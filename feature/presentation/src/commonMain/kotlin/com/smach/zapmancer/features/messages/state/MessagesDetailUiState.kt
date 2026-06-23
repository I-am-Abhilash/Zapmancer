package com.smach.zapmancer.features.messages.state

import com.smach.zapmancer.domain.model.MessageItem

data class MessagesDetailUiState(
    val contactName: String = "Jordan Smith",
    val contactAvatarUrl: String = "",
    val isOnline: Boolean = true,
    val messages: List<MessageItem> = emptyList(),
    val typingText: String = "",
    val isContactTyping: Boolean = false,
    val isLoading: Boolean = false
)



