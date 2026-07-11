package com.smach.zapmancer.features.alerts.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.NotificationAction
import com.smach.zapmancer.domain.model.NotificationItem
import com.smach.zapmancer.domain.model.NotificationType
import com.smach.zapmancer.domain.usecase.ExecuteNotificationActionUseCase
import com.smach.zapmancer.domain.usecase.GetNotificationsUseCase
import com.smach.zapmancer.domain.usecase.SendNotificationQuickReplyUseCase
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import kotlinx.coroutines.launch

sealed class NotificationEvent {
    data object Refresh : NotificationEvent()
    data class OnReplyTextChanged(val notificationId: String, val text: String) : NotificationEvent()

    data class SendQuickReply(val notificationId: String) : NotificationEvent()
    data class ExecuteAction(val notificationId: String, val actionLabel: String) : NotificationEvent()
    data object BackClicked : NotificationEvent()
}

sealed class NotificationEffect {
    data class ShowToast(val message: String) : NotificationEffect()
    data object NavigateBack : NotificationEffect()
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
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getNotificationsUseCase().foldTyped(
                onSuccess = { data ->
                    updateState {
                        copy(
                            notifications = data.map { domainItem ->
                                NotificationItem(
                                    id = domainItem.id,
                                    type = NotificationType.valueOf(domainItem.type.name),
                                    title = domainItem.title,
                                    description = domainItem.description,
                                    timestamp = domainItem.timestamp,
                                    section = domainItem.section,
                                    codeSnippet = domainItem.codeSnippet,
                                    isItalic = domainItem.isItalic,
                                    actions = domainItem.actions.map { domainAction ->
                                        NotificationAction(
                                            label = domainAction.label,
                                            isPrimary = domainAction.isPrimary,
                                            isError = domainAction.isError,
                                        )
                                    },
                                    quickReply = domainItem.quickReply,
                                )
                            },
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
            updateState { copy(isLoading = true, error = null) }
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

    private fun executeAction(notificationId: String, actionLabel: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            executeNotificationActionUseCase(notificationId, actionLabel).foldTyped(
                onSuccess = {
                    updateState { copy(isLoading = false) }
                    sendEffect(NotificationEffect.ShowToast("Action executed: $actionLabel"))
                    loadNotifications()
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
}
