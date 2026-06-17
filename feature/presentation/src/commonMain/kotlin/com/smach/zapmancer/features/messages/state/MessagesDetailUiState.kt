package com.smach.zapmancer.features.messages.state

data class MessagesDetailUiState(
    val contactName: String = "",
    val contactAvatarUrl: String = "",
    val isOnline: Boolean = false,
    val messages: List<MessageItem> = emptyList(),
    val typingText: String = "",
    val isContactTyping: Boolean = false,
    val isLoading: Boolean = false
)

data class MessageItem(
    val id: String,
    val text: String,
    val timestamp: String,
    val isFromMe: Boolean,
    val status: MessageStatus = MessageStatus.SENT,
    val avatarUrl: String? = null // For other's messages
)

enum class MessageStatus {
    SENT, DELIVERED, READ
}
