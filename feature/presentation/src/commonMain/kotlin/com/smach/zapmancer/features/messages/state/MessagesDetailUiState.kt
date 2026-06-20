package com.smach.zapmancer.features.messages.state

data class MessagesDetailUiState(
    val contactName: String = "Jordan Smith",
    val contactAvatarUrl: String = "",
    val isOnline: Boolean = true,
    val messages: List<MessageItem> = com.smach.zapmancer.features.messages.state.messages,
    val typingText: String = "",
    val isContactTyping: Boolean = false,
    val isLoading: Boolean = false
)

val messages = listOf(
    MessageItem(
        id = "1",
        text = "The deployment pipeline is successfully configured for the staging environment. Can you check the logs to verify everything is running smoothly?",
        timestamp = "09:42 AM",
        isFromMe = false
    ),
    MessageItem(
        id = "2",
        text = "I'm on it. Just logging into the dashboard now. The CPU spikes we saw yesterday shouldn't be an issue with the new load balancer config.",
        timestamp = "09:45 AM",
        isFromMe = true,
        status = MessageStatus.READ
    ),
    MessageItem(
        id = "3",
        text = "Agreed. Let me know if you see any anomalies in the memory footprint. I'll be around for the next hour.",
        timestamp = "09:46 AM",
        isFromMe = false
    )
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
