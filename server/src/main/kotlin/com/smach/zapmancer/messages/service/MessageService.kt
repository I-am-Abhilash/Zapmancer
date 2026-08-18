package com.smach.zapmancer.messages.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.AttachmentUploadResponse
import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.ConversationItem
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.core.framework.storage.StorageService
import com.smach.zapmancer.messages.repository.MessageRepository
import org.koin.core.annotation.Single
import java.util.UUID
import kotlin.time.Clock

@Single
class MessageService(
    private val repository: MessageRepository,
    private val connectionManager: ConnectionManager,
    private val storageService: StorageService? = null,
    private val bucketName: String = "zapmancer-assets",
) {

    suspend fun getConversations(userId: String): List<ConversationItem> {
        val conversations = repository.getConversations(userId)
        return conversations.map { conv ->
            val participants = repository.getConversationParticipants(conv.id)
            val otherId = if (participants != null) {
                if (userId == participants.first) participants.second else participants.first
            } else {
                null
            }

            val isOnline = if (otherId != null) connectionManager.isUserOnline(otherId) else false
            conv.copy(isOnline = isOnline)
        }
    }

    suspend fun getMessages(
        conversationId: String,
        userId: String,
        limit: Int = 50,
    ): List<MessageItem> {
        val participants = repository.getConversationParticipants(conversationId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Conversation not found.")

        if (participants.first != userId && participants.second != userId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not a participant in this conversation.")
        }
        return repository.getMessages(conversationId, userId, limit)
    }

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        text: String,
        attachmentUrl: String? = null,
        attachmentType: String? = null,
        attachmentName: String? = null,
        attachmentSizeBytes: Long? = null,
        replyToMessageId: String? = null,
    ): CommonResponse {
        val participants = repository.getConversationParticipants(conversationId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Conversation not found.")

        if (participants.first != senderId && participants.second != senderId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not a participant in this conversation.")
        }

        val messageId = repository.sendMessage(
            conversationId = conversationId,
            senderId = senderId,
            text = text,
            attachmentUrl = attachmentUrl,
            attachmentType = attachmentType,
            attachmentName = attachmentName,
            attachmentSizeBytes = attachmentSizeBytes,
            replyToMessageId = replyToMessageId,
        )

        val recipientId = if (senderId == participants.first) participants.second else participants.first

        val recipientMsg = repository.getMessage(messageId, recipientId)
        if (recipientMsg != null) {
            connectionManager.sendToUser(
                recipientId,
                ChatFrame.ServerToClient.NewMessage(conversationId, recipientMsg),
            )
        }

        val senderMsg = repository.getMessage(messageId, senderId)
        if (senderMsg != null) {
            connectionManager.sendToUser(
                senderId,
                ChatFrame.ServerToClient.NewMessage(conversationId, senderMsg),
            )
        }

        return CommonResponse(success = true, message = "Message sent.")
    }

    suspend fun handleTyping(conversationId: String, senderId: String, isTyping: Boolean) {
        connectionManager.setTyping(conversationId, senderId, isTyping)

        val participants = repository.getConversationParticipants(conversationId)
        if (participants != null) {
            val recipientId = if (senderId == participants.first) participants.second else participants.first
            connectionManager.sendToUser(
                recipientId,
                ChatFrame.ServerToClient.TypingUpdate(conversationId, senderId, isTyping),
            )
        }
    }

    suspend fun handleReact(conversationId: String, userId: String, messageId: String, emoji: String) {
        val updatedReactions = repository.toggleReaction(messageId, userId, emoji)

        val participants = repository.getConversationParticipants(conversationId)
        if (participants != null) {
            val frame = ChatFrame.ServerToClient.ReactionUpdate(conversationId, messageId, updatedReactions)
            connectionManager.sendToUser(participants.first, frame)
            connectionManager.sendToUser(participants.second, frame)
        }
    }

    suspend fun handleEditMessage(conversationId: String, senderId: String, messageId: String, newText: String) {
        val updated = repository.editMessage(messageId, senderId, newText)
        if (updated) {
            val participants = repository.getConversationParticipants(conversationId)
            if (participants != null) {
                val frame = ChatFrame.ServerToClient.MessageEdited(conversationId, messageId, newText, isEdited = true)
                connectionManager.sendToUser(participants.first, frame)
                connectionManager.sendToUser(participants.second, frame)
            }
        }
    }

    suspend fun handleDeleteMessage(conversationId: String, senderId: String, messageId: String) {
        val deleted = repository.deleteMessage(messageId, senderId)
        if (deleted) {
            val participants = repository.getConversationParticipants(conversationId)
            if (participants != null) {
                val frame = ChatFrame.ServerToClient.MessageDeleted(conversationId, messageId, isDeleted = true)
                connectionManager.sendToUser(participants.first, frame)
                connectionManager.sendToUser(participants.second, frame)
            }
        }
    }

    suspend fun markMessageRead(conversationId: String, readerId: String, messageId: String): CommonResponse {
        val senderId = repository.markMessageRead(messageId, readerId)
        if (senderId != null) {
            val nowTime = Clock.System.now().toString().take(16).replace("T", " ")
            connectionManager.sendToUser(
                senderId,
                ChatFrame.ServerToClient.MessageStatusUpdate(conversationId, messageId, "READ", readAt = nowTime),
            )
        }
        return CommonResponse(success = true, message = "Message marked as read.")
    }

    suspend fun markAllRead(conversationId: String, userId: String): CommonResponse {
        val participants = repository.getConversationParticipants(conversationId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Conversation not found.")

        if (participants.first != userId && participants.second != userId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not a participant in this conversation.")
        }

        repository.markAllRead(conversationId, userId)
        val otherId = if (userId == participants.first) participants.second else participants.first
        connectionManager.sendToUser(
            otherId,
            ChatFrame.ServerToClient.MessageStatusUpdate(conversationId, "", "READ"),
        )

        return CommonResponse(success = true, message = "Conversation marked as read.")
    }

    suspend fun uploadAttachment(
        userId: String,
        fileName: String,
        fileBytes: ByteArray,
        contentType: String,
    ): AttachmentUploadResponse {
        val ext = fileName.substringAfterLast(".", "")
        val path = "attachments/$userId/${UUID.randomUUID()}.$ext"

        val publicUrl = storageService?.uploadFile(bucketName, path, fileBytes, contentType)
            ?: "/static/$path"

        val type = when {
            contentType.startsWith("image/") -> "IMAGE"
            contentType.startsWith("audio/") -> "AUDIO"
            else -> "FILE"
        }

        return AttachmentUploadResponse(
            url = publicUrl,
            fileName = fileName,
            fileType = type,
            sizeBytes = fileBytes.size.toLong(),
        )
    }

    suspend fun getConversationParticipants(conversationId: String): Pair<String, String>? = repository.getConversationParticipants(conversationId)
}
