package com.smach.zapmancer.features.messages.state

import com.smach.zapmancer.domain.model.ConversationItem

data class MessagesListUiState(
    val conversations: List<ConversationItem> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: String = "All",
    val isLoading: Boolean = false,
    val error: String? = null,
)

fun com.smach.zapmancer.domain.model.ConversationItem.toUiModel(): com.smach.zapmancer.domain.model.ConversationItem = this
