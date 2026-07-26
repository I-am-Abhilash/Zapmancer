package com.smach.zapmancer.presentation.messages.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.GetMessagesUseCase
import com.smach.zapmancer.domain.usecase.MarkConversationAsReadUseCase
import com.smach.zapmancer.domain.usecase.ObservePresenceUseCase
import com.smach.zapmancer.domain.usecase.ObserveTypingUseCase
import com.smach.zapmancer.domain.usecase.SendMessageUseCase
import com.smach.zapmancer.domain.usecase.SendTypingStatusUseCase
import com.smach.zapmancer.presentation.messages.state.MessagesDetailUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

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


@KoinViewModel
class MessagesDetailViewModel(
    @InjectedParam private val conversationId: String,
    @InjectedParam private val contactName: String,
    @InjectedParam private val contactAvatarUrl: String,
    @InjectedParam private val isOnline: Boolean,
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val markConversationAsReadUseCase: MarkConversationAsReadUseCase,
    private val observePresenceUseCase: ObservePresenceUseCase,
    private val observeTypingUseCase: ObserveTypingUseCase,
    private val sendTypingStatusUseCase: SendTypingStatusUseCase,
) : BaseViewModel<MessagesDetailUiState, MessagesDetailEvent, MessagesDetailEffect>(
    MessagesDetailUiState(
        contactName = contactName,
        contactAvatarUrl = contactAvatarUrl,
        isOnline = isOnline,
    ),
) {

    private var typingJob: Job? = null
    private var isCurrentlyTyping = false

    init {
        observeMessages()
        observeRealtimeUpdates()
        markAsRead()
    }

    override fun onEvent(event: MessagesDetailEvent) {
        when (event) {
            is MessagesDetailEvent.OnTextChanged -> {
                updateState { copy(typingText = event.text) }
                handleTyping(event.text)
            }

            MessagesDetailEvent.SendMessage -> sendMessage()

            MessagesDetailEvent.MarkAsRead -> markAsRead()

            MessagesDetailEvent.BackClicked -> sendEffect(MessagesDetailEffect.NavigateBack)

            is MessagesDetailEvent.ProfileClicked -> sendEffect(
                MessagesDetailEffect.NavigateToProfile(
                    event.contactName,
                ),
            )
        }
    }

    private fun handleTyping(text: String) {
        typingJob?.cancel()
        viewModelScope.launch {
            if (text.isNotEmpty() && !isCurrentlyTyping) {
                isCurrentlyTyping = true
                sendTypingStatusUseCase(conversationId, true)
            }
        }
        typingJob = viewModelScope.launch {
            delay(3000)
            if (isCurrentlyTyping) {
                isCurrentlyTyping = false
                sendTypingStatusUseCase(conversationId, false)
            }
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

    private fun observeRealtimeUpdates() {
        viewModelScope.launch {
            observePresenceUseCase(conversationId).collectLatest { isOnline ->
                updateState { copy(isOnline = isOnline) }
            }
        }
        viewModelScope.launch {
            observeTypingUseCase(conversationId).collectLatest { isTyping ->
                updateState { copy(isContactTyping = isTyping) }
            }
        }
    }

    private fun sendMessage() {
        val text = uiState.value.typingText
        if (text.isBlank()) return

        viewModelScope.launch {
            updateState { copy(typingText = "") }
            typingJob?.cancel()
            if (isCurrentlyTyping) {
                isCurrentlyTyping = false
                sendTypingStatusUseCase(conversationId, false)
            }
            sendMessageUseCase(conversationId, text)
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            markConversationAsReadUseCase(conversationId)
        }
    }
}
