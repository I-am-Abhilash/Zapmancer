package com.smach.zapmancer.features.messages.state

data class MessagesListUiState(
    val conversations: List<ConversationItem> = com.smach.zapmancer.features.messages.state.conversations,
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val isLoading: Boolean = false
)

val conversations: List<ConversationItem> = listOf(
    ConversationItem(
        id = "1",
        name = "Alex Rivera",
        avatarUrl = "",
        lastMessage = "The deployment pipeline is successfully configured...",
        timestamp = "09:42 AM",
        isUnread = true,
        isOnline = true
    ),
    ConversationItem(
        id = "2",
        name = "Sarah Chen",
        avatarUrl = "",
        lastMessage = "I've reviewed the latest pull request. Just a few minor...",
        timestamp = "Yesterday",
        isUnread = false,
        isOnline = false
    ),
    ConversationItem(
        id = "3",
        name = "Jordan Smith",
        avatarUrl = "",
        lastMessage = "Are we still on for the sync at 2 PM?",
        timestamp = "Monday",
        isUnread = false,
        isOnline = true
    )
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
