package com.smach.zapmancer.features.alerts.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.ExecuteNotificationActionUseCase
import com.smach.zapmancer.domain.usecase.GetNotificationsUseCase
import com.smach.zapmancer.domain.usecase.SendNotificationQuickReplyUseCase
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import kotlinx.coroutines.launch

sealed interface NotificationEvent {
    data object Refresh : NotificationEvent
    data class OnReplyTextChanged(val notificationId: String, val text: String) : NotificationEvent

    data class SendQuickReply(val notificationId: String) : NotificationEvent
    data class ExecuteAction(val notificationId: String, val actionLabel: String) : NotificationEvent

    data object BackClicked : NotificationEvent
}

sealed interface NotificationEffect {
    data class ShowToast(val message: String) : NotificationEffect
    data object NavigateBack : NotificationEffect
}

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val executeNotificationActionUseCase: ExecuteNotificationActionUseCase,
    private val sendNotificationQuickReplyUseCase: SendNotificationQuickReplyUseCase,
) : BaseViewModel<NotificationUiState, NotificationEvent, NotificationEffect>(NotificationUiState()) {

    init {
        loadNotifications()
    }

    override fun onEvent(event: NotificationEvent) {
        when (event) {
            NotificationEvent.Refresh -> loadNotifications()

            is NotificationEvent.OnReplyTextChanged -> {
                updateState {
                    copy(replyDrafts = replyDrafts + (event.notificationId to event.text))
                }
            }

            is NotificationEvent.SendQuickReply -> sendQuickReply(event.notificationId)

            is NotificationEvent.ExecuteAction -> executeAction(
                event.notificationId,
                event.actionLabel,
            )

            NotificationEvent.BackClicked -> sendEffect(NotificationEffect.NavigateBack)
        }
    }

    private fun loadNotifications() {
        if (uiState.value.isLoading) return
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getNotificationsUseCase().foldTyped(
                onSuccess = { data ->
                    updateState {
                        copy(
                            notifications = data,
                            isLoading = false,
                        )
                    }
                },
                onError = { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = error.toUserMessage(),
                        )
                    }
                },
            )
        }
    }

    private fun sendQuickReply(notificationId: String) {
        val replyText = uiState.value.replyDrafts[notificationId].orEmpty()
        if (replyText.isBlank()) return

        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            sendNotificationQuickReplyUseCase(notificationId, replyText).foldTyped(
                onSuccess = {
                    updateState {
                        copy(
                            isLoading = false,
                            replyDrafts = replyDrafts - notificationId,
                        )
                    }
                    sendEffect(NotificationEffect.ShowToast("Quick reply sent!"))
                    loadNotifications()
                },
                onError = { error ->
                    updateState { copy(isLoading = false) }
                    sendEffect(NotificationEffect.ShowToast("Failed to send quick reply: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun executeAction(notificationId: String, actionLabel: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            executeNotificationActionUseCase(notificationId, actionLabel).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false) }
                    sendEffect(NotificationEffect.ShowToast("Action executed: $actionLabel"))
                    loadNotifications()
                },
                onError = { error ->
                    updateState { copy(isLoading = false) }
                    sendEffect(NotificationEffect.ShowToast("Failed to execute action: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
