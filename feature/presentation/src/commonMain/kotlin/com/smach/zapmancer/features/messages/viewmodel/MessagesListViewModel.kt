package com.smach.zapmancer.features.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.usecase.GetConversationsUseCase
import com.smach.zapmancer.features.messages.state.ConversationItem
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import kotlinx.coroutines.launch

sealed class MessagesListEvent {
    data object Refresh : MessagesListEvent()
    data class OnSearchQueryChanged(val query: String) : MessagesListEvent()
    data class OnFilterSelected(val filter: String) : MessagesListEvent()
}

class MessagesListViewModel(
    private val getConversationsUseCase: GetConversationsUseCase
) : BaseViewModel<MessagesListUiState, MessagesListEvent, Unit>(MessagesListUiState()) {

    init {
        loadConversations()
    }

    override fun onEvent(event: MessagesListEvent) {
        when (event) {
            MessagesListEvent.Refresh -> loadConversations()
            is MessagesListEvent.OnSearchQueryChanged -> {
                updateState { copy(searchQuery = event.query) }
            }

            is MessagesListEvent.OnFilterSelected -> {
                updateState { copy(selectedFilter = event.filter) }
            }
        }
    }

    private fun loadConversations() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            when (val result = getConversationsUseCase()) {
                is Result.Success -> {
                    updateState {
                        copy(
                            conversations = result.data.map { domainItem ->
                                ConversationItem(
                                    id = domainItem.id,
                                    name = domainItem.name,
                                    avatarUrl = domainItem.avatarUrl,
                                    lastMessage = domainItem.lastMessage,
                                    timestamp = domainItem.timestamp,
                                    isUnread = domainItem.isUnread,
                                    isOnline = domainItem.isOnline
                                )
                            },
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    updateState { copy(isLoading = false) }
                }
            }
        }
    }
}
