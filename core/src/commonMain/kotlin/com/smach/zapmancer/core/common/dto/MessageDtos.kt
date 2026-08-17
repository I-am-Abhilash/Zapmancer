package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConversationItem(
    val id: String,
    val name: String,
    val avatarUrl: String?,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean,
    val isOnline: Boolean,
)

@Serializable
data class MessageReactionItem(
    val emoji: String,
    val count: Int,
    val userIds: List<String> = emptyList(),
    val isMine: Boolean = false,
)

@Serializable
data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    /** SENT | DELIVERED | READ */
    val status: String,
    val avatarUrl: String?,
    val attachmentUrl: String? = null,
    /** IMAGE | FILE | AUDIO */
    val attachmentType: String? = null,
    val attachmentName: String? = null,
    val attachmentSizeBytes: Long? = null,
    val replyToMessageId: String? = null,
    val replyToSnippet: String? = null,
    val reactions: List<MessageReactionItem> = emptyList(),
    val isEdited: Boolean = false,
    val isDeleted: Boolean = false,
    val readAt: String? = null,
)

@Serializable
data class SendMessageRequest(
    val text: String,
    val attachmentUrl: String? = null,
    val attachmentType: String? = null,
    val attachmentName: String? = null,
    val attachmentSizeBytes: Long? = null,
    val replyToMessageId: String? = null,
)

@Serializable
data class AttachmentUploadResponse(
    val url: String,
    val fileName: String,
    val fileType: String,
    val sizeBytes: Long,
)

@Serializable
sealed interface ChatFrame {
    @Serializable
    sealed interface ClientToServer : ChatFrame {
        @Serializable
        data class SendMessage(
            val conversationId: String,
            val text: String,
            val attachmentUrl: String? = null,
            val attachmentType: String? = null,
            val attachmentName: String? = null,
            val attachmentSizeBytes: Long? = null,
            val replyToMessageId: String? = null,
        ) : ClientToServer

        @Serializable
        data class Typing(
            val conversationId: String,
            val isTyping: Boolean,
        ) : ClientToServer

        @Serializable
        data class MarkRead(
            val conversationId: String,
            val messageId: String,
        ) : ClientToServer

        @Serializable
        data class React(
            val conversationId: String,
            val messageId: String,
            val emoji: String,
        ) : ClientToServer

        @Serializable
        data class EditMessage(
            val conversationId: String,
            val messageId: String,
            val newText: String,
        ) : ClientToServer

        @Serializable
        data class DeleteMessage(
            val conversationId: String,
            val messageId: String,
        ) : ClientToServer
    }

    @Serializable
    sealed interface ServerToClient : ChatFrame {
        @Serializable
        data class NewMessage(
            val conversationId: String,
            val message: MessageItem,
        ) : ServerToClient

        @Serializable
        data class MessageStatusUpdate(
            val conversationId: String,
            val messageId: String,
            val status: String,
            val readAt: String? = null,
        ) : ServerToClient

        @Serializable
        data class TypingUpdate(
            val conversationId: String,
            val userId: String,
            val isTyping: Boolean,
        ) : ServerToClient

        @Serializable
        data class PresenceUpdate(
            val conversationId: String,
            val userId: String,
            val isOnline: Boolean,
            val lastSeen: String? = null,
        ) : ServerToClient

        @Serializable
        data class ReactionUpdate(
            val conversationId: String,
            val messageId: String,
            val reactions: List<MessageReactionItem>,
        ) : ServerToClient

        @Serializable
        data class MessageEdited(
            val conversationId: String,
            val messageId: String,
            val newText: String,
            val isEdited: Boolean = true,
        ) : ServerToClient

        @Serializable
        data class MessageDeleted(
            val conversationId: String,
            val messageId: String,
            val isDeleted: Boolean = true,
        ) : ServerToClient

        @Serializable
        data class Error(val reason: String) : ServerToClient
    }
}
