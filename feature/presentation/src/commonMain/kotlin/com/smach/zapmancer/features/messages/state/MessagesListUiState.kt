package com.smach.zapmancer.features.messages.state

data class MessagesListUiState(
    val conversations: List<ConversationItem> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val isLoading: Boolean = false
)

data class ConversationItem(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val lastMessage: String,
    val timestamp: String,
    val isUnread: Boolean = false,
    val isOnline: Boolean = false
)
