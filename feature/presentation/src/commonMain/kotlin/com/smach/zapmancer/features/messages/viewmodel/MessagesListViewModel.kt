package com.smach.zapmancer.features.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.usecase.GetConversationsUseCase
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import kotlinx.coroutines.launch

sealed class MessagesListEvent {
    data object Refresh : MessagesListEvent()
    data class OnSearchQueryChanged(val query: String) : MessagesListEvent()
    data class OnFilterSelected(val filter: String) : MessagesListEvent()
    data class ConversationClicked(val id: String) : MessagesListEvent()
    data class ProfileClicked(val id: String) : MessagesListEvent()
}

sealed class MessagesListEffect {
    data class NavigateToConversation(val id: String) : MessagesListEffect()
    data class NavigateToProfile(val id: String) : MessagesListEffect()
}

class MessagesListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase,
) : BaseViewModel<MessagesListUiState, MessagesListEvent, MessagesListEffect>(MessagesListUiState()) {

    override fun onEvent(event: MessagesListEvent) {
        when (event) {
            MessagesListEvent.Refresh -> loadConversations()

            is MessagesListEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
            }

            is MessagesListEvent.OnFilterSelected -> {
                updateState { copy(selectedFilter = event.filter) }
            }

            is MessagesListEvent.ConversationClicked -> {
                sendEffect(MessagesListEffect.NavigateToConversation(event.id))
            }

            is MessagesListEvent.ProfileClicked -> {
                sendEffect(MessagesListEffect.NavigateToProfile(event.id))
            }
        }
    }

    init {
        loadConversations()
    }

    private fun loadConversations() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getConversationsUseCase().foldTyped(
                onSuccess = { list ->
                    updateState {
                        copy(
                            conversations = list.map { domainItem ->
                                ConversationItem(
                                    id = domainItem.id,
                                    name = domainItem.name,
                                    avatarUrl = domainItem.avatarUrl,
                                    lastMessage = domainItem.lastMessage,
                                    timestamp = domainItem.timestamp,
                                    isUnread = domainItem.isUnread,
                                    isOnline = domainItem.isOnline,
                                )
                            },
                            isLoading = false,
                        )
                    }
                },
                onError = { updateState { copy(isLoading = false) } },
            )
        }
    }
}
