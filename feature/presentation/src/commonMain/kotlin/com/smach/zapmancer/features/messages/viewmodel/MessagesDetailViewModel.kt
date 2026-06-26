package com.smach.zapmancer.features.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.model.MessageStatus
import com.smach.zapmancer.domain.usecase.GetMessagesUseCase
import com.smach.zapmancer.domain.usecase.MarkConversationAsReadUseCase
import com.smach.zapmancer.domain.usecase.SendMessageUseCase
import com.smach.zapmancer.features.messages.state.MessagesDetailUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class MessagesDetailEvent {
    data class OnTextChanged(val text: String) : MessagesDetailEvent()
    data object SendMessage : MessagesDetailEvent()
    data object MarkAsRead : MessagesDetailEvent()
}

class MessagesDetailViewModel(
    private val conversationId: String,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markConversationAsReadUseCase: MarkConversationAsReadUseCase,
) : BaseViewModel<MessagesDetailUiState, MessagesDetailEvent, Unit>(MessagesDetailUiState()) {

    override fun onEvent(event: MessagesDetailEvent) {
        when (event) {
            is MessagesDetailEvent.OnTextChanged -> {
                updateState { copy(typingText = event.text) }
            }

            MessagesDetailEvent.SendMessage -> sendMessage()

            MessagesDetailEvent.MarkAsRead -> markAsRead()
        }
    }

    init {
        observeMessages()
        markAsRead()
    }
    private fun observeMessages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getMessagesUseCase(conversationId).collectLatest { domainMessages ->
                updateState {
                    copy(
                        messages = domainMessages.map { domainItem ->
                            MessageItem(
                                id = domainItem.id,
                                text = domainItem.text,
                                timestamp = domainItem.timestamp,
                                isFromMe = domainItem.isFromMe,
                                status = MessageStatus.valueOf(domainItem.status.name),
                                avatarUrl = domainItem.avatarUrl,
                            )
                        },
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun sendMessage() {
        val text = uiState.value.typingText
        if (text.isBlank()) return

        viewModelScope.launch {
            updateState { copy(typingText = "") }
            sendMessageUseCase(conversationId, text)
            // Result intentionally ignored; UI re-collects messages from getMessagesUseCase flow.
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            markConversationAsReadUseCase(conversationId)
        }
    }
}
