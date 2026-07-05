package com.smach.zapmancer.features.messages.state

import com.smach.zapmancer.domain.model.MessageItem

data class MessagesDetailUiState(
    val contactName: String = "",
    val contactAvatarUrl: String = "",
    val isOnline: Boolean = false,
    val messages: List<MessageItem> = emptyList(),
    val typingText: String = "",
    val isContactTyping: Boolean = false,
    val conversationDate: String = "",
    val isLoading: Boolean = false,
)
