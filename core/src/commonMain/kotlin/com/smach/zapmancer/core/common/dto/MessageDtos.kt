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
data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    /** SENT | DELIVERED | READ */
    val status: String,
    val avatarUrl: String?,
)

@Serializable
data class SendMessageRequest(val text: String)

@Serializable
sealed interface ChatFrame {
    @Serializable
    sealed interface ClientToServer : ChatFrame {
        @Serializable
        data class Typing(val conversationId: String, val isTyping: Boolean) : ClientToServer
    }

    @Serializable
    sealed interface ServerToClient : ChatFrame {
        @Serializable
        data class NewMessage(val conversationId: String, val message: MessageItem) : ServerToClient

        @Serializable
        data class MessageStatusUpdate(
            val conversationId: String,
            val messageId: String,
            val status: String,
        ) : ServerToClient

        @Serializable
        data class TypingUpdate(
            val conversationId: String,
            val isTyping: Boolean,
        ) : ServerToClient

        @Serializable
        data class PresenceUpdate(
            val conversationId: String,
            val isOnline: Boolean,
        ) : ServerToClient

        @Serializable
        data class Error(val reason: String) : ServerToClient
    }
}
