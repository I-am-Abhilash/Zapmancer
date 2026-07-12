package com.smach.zapmancer.features.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.usecase.GetConversationsUseCase
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import com.smach.zapmancer.features.messages.state.toUiModel
import kotlinx.coroutines.launch

sealed interface MessagesListEvent {
    data object Refresh : MessagesListEvent
    data class OnSearchQueryChanged(val query: String) : MessagesListEvent
    data class OnFilterSelected(val filter: String) : MessagesListEvent
    data class ConversationClicked(val id: String) : MessagesListEvent
    data class ProfileClicked(val id: String) : MessagesListEvent
}

sealed interface MessagesListEffect {
    data class NavigateToConversation(val id: String) : MessagesListEffect
    data class NavigateToProfile(val id: String) : MessagesListEffect
    data class ShowToast(val message: String) : MessagesListEffect
}

class MessagesListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase,
) : BaseViewModel<MessagesListUiState, MessagesListEvent, MessagesListEffect>(MessagesListUiState()) {

    private var allConversations: List<ConversationItem> = emptyList()

    init {
        loadConversations()
    }

    override fun onEvent(event: MessagesListEvent) {
        when (event) {
            MessagesListEvent.Refresh -> loadConversations()

            is MessagesListEvent.OnSearchQueryChanged -> {
                updateState {
                    copy(
                        searchQuery = event.query,
                        conversations = filterConversations(
                            allConversations,
                            event.query,
                            selectedFilter
                        ),
                    )
                }
            }

            is MessagesListEvent.OnFilterSelected -> {
                updateState {
                    copy(
                        selectedFilter = event.filter,
                        conversations = filterConversations(
                            allConversations,
                            searchQuery,
                            event.filter
                        ),
                    )
                }
            }

            is MessagesListEvent.ConversationClicked -> {
                sendEffect(MessagesListEffect.NavigateToConversation(event.id))
            }

            is MessagesListEvent.ProfileClicked -> {
                sendEffect(MessagesListEffect.NavigateToProfile(event.id))
            }
        }
    }

    private fun filterConversations(
        list: List<ConversationItem>,
        query: String,
        filter: String,
    ): List<ConversationItem> = list.filter {
        val matchesQuery = it.name.contains(query, ignoreCase = true) ||
                it.lastMessage.contains(query, ignoreCase = true)
        val matchesFilter = when (filter) {
            "Unread" -> it.isUnread
            "Online" -> it.isOnline
            else -> true
        }
        matchesQuery && matchesFilter
    }

    private fun loadConversations() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getConversationsUseCase().foldTyped(
                onSuccess = { list ->
                    val mappedList = list.map { it.toUiModel() }
                    allConversations = mappedList
                    updateState {
                        copy(
                            conversations = filterConversations(
                                mappedList,
                                searchQuery,
                                selectedFilter
                            ),
                            isLoading = false,
                        )
                    }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(MessagesListEffect.ShowToast("Failed to load conversations: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
