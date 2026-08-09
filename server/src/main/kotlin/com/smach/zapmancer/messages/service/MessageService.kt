package com.smach.zapmancer.messages.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.DomainResult
import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.ConversationItem
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.messages.repository.MessageRepository
import org.koin.core.annotation.Single

@Single
class MessageService(
    private val repository: MessageRepository,
    private val connectionManager: ConnectionManager,
) {

    suspend fun getConversations(userId: String): DomainResult<List<ConversationItem>> = DomainResult.Success(repository.getConversations(userId))

    suspend fun getMessages(
        conversationId: String,
        userId: String,
    ): DomainResult<List<MessageItem>> = DomainResult.Success(repository.getMessages(conversationId, userId))

    suspend fun sendMessage(
        conversationId: String,
        senderId: String,
        text: String,
    ): DomainResult<CommonResponse> {
        val messageId = repository.sendMessage(conversationId, senderId, text)

        val participants = repository.getConversationParticipants(conversationId)
        if (participants != null) {
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
        }

        return DomainResult.Success(CommonResponse(success = true, message = "Message sent."))
    }

    suspend fun markRead(conversationId: String, userId: String): DomainResult<CommonResponse> {
        repository.markRead(conversationId, userId)

        val participants = repository.getConversationParticipants(conversationId)
        if (participants != null) {
            val otherId = if (userId == participants.first) participants.second else participants.first
            connectionManager.sendToUser(
                otherId,
                ChatFrame.ServerToClient.MessageStatusUpdate(conversationId, "", "READ"),
            )
        }

        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "Conversation marked as read.",
            ),
        )
    }

    suspend fun getConversationParticipants(conversationId: String): Pair<String, String>? = repository.getConversationParticipants(conversationId)
}
