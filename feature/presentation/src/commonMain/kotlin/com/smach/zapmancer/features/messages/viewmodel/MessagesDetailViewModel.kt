package com.smach.zapmancer.features.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.GetMessagesUseCase
import com.smach.zapmancer.domain.usecase.MarkConversationAsReadUseCase
import com.smach.zapmancer.domain.usecase.SendMessageUseCase
import com.smach.zapmancer.features.messages.state.MessagesDetailUiState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed interface MessagesDetailEvent {
    data class OnTextChanged(val text: String) : MessagesDetailEvent
    data object SendMessage : MessagesDetailEvent
    data object MarkAsRead : MessagesDetailEvent
    data object BackClicked : MessagesDetailEvent
    data class ProfileClicked(val contactName: String) : MessagesDetailEvent
}

sealed interface MessagesDetailEffect {
    data object NavigateBack : MessagesDetailEffect
    data class NavigateToProfile(val contactName: String) : MessagesDetailEffect
}

class MessagesDetailViewModel(
    private val conversationId: String,
    private val contactName: String,
    private val contactAvatarUrl: String,
    private val isOnline: Boolean,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markConversationAsReadUseCase: MarkConversationAsReadUseCase,
) : BaseViewModel<MessagesDetailUiState, MessagesDetailEvent, MessagesDetailEffect>(
    MessagesDetailUiState(
        contactName = contactName,
        contactAvatarUrl = contactAvatarUrl,
        isOnline = isOnline,
    ),
) {

    init {
        observeMessages()
        markAsRead()
    }

    override fun onEvent(event: MessagesDetailEvent) {
        when (event) {
            is MessagesDetailEvent.OnTextChanged -> {
                updateState { copy(typingText = event.text) }
            }

            MessagesDetailEvent.SendMessage -> sendMessage()

            MessagesDetailEvent.MarkAsRead -> markAsRead()

            MessagesDetailEvent.BackClicked -> sendEffect(MessagesDetailEffect.NavigateBack)

            is MessagesDetailEvent.ProfileClicked -> sendEffect(
                MessagesDetailEffect.NavigateToProfile(
                    event.contactName
                )
            )
        }
    }

    private fun observeMessages() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getMessagesUseCase(conversationId).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        val domainMessages = result.data
                        val date = domainMessages.firstOrNull()?.timestamp.orEmpty()
                        updateState {
                            copy(
                                messages = domainMessages,
                                conversationDate = date,
                                isLoading = false,
                                error = null,
                            )
                        }
                    }

                    is Result.Error -> {
                        updateState {
                            copy(
                                isLoading = false,
                                error = result.error.toUserMessage(),
                            )
                        }
                    }
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
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            markConversationAsReadUseCase(conversationId)
        }
    }
}
