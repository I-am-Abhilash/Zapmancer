package com.smach.zapmancer.features.alerts.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.usecase.ExecuteNotificationActionUseCase
import com.smach.zapmancer.domain.usecase.GetNotificationsUseCase
import com.smach.zapmancer.domain.usecase.SendNotificationQuickReplyUseCase
import com.smach.zapmancer.features.alerts.state.NotificationAction
import com.smach.zapmancer.features.alerts.state.NotificationItem
import com.smach.zapmancer.features.alerts.state.NotificationType
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import kotlinx.coroutines.launch

sealed class NotificationEvent {
    data object Refresh : NotificationEvent()
    data class OnReplyTextChanged(val notificationId: String, val text: String) :
        NotificationEvent()

    data class SendQuickReply(val notificationId: String) : NotificationEvent()
    data class ExecuteAction(val notificationId: String, val actionLabel: String) :
        NotificationEvent()
}

sealed class NotificationEffect {
    data class ShowToast(val message: String) : NotificationEffect()
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
                event.actionLabel
            )
        }
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = getNotificationsUseCase()) {
                is Result.Success -> {
                    updateState {
                        copy(
                            notifications = result.data.map { domainItem ->
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
                                            isError = domainAction.isError
                                        )
                                    },
                                    quickReply = domainItem.quickReply
                                )
                            },
                            isLoading = false
                        )
                    }
                }

                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Failed to load notifications"
                        )
                    }
                }
            }
        }
    }

    private fun sendQuickReply(notificationId: String) {
        val replyText = uiState.value.replyDrafts[notificationId].orEmpty()
        if (replyText.isBlank()) return

        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = sendNotificationQuickReplyUseCase(notificationId, replyText)) {
                is Result.Success -> {
                    updateState {
                        copy(
                            isLoading = false,
                            replyDrafts = replyDrafts - notificationId
                        )
                    }
                    sendEffect(NotificationEffect.ShowToast("Quick reply sent!"))
                    loadNotifications()
                }

                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Failed to send quick reply"
                        )
                    }
                }
            }
        }
    }

    private fun executeAction(notificationId: String, actionLabel: String) {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = executeNotificationActionUseCase(notificationId, actionLabel)) {
                is Result.Success -> {
                    updateState { copy(isLoading = false) }
                    sendEffect(NotificationEffect.ShowToast("Action executed: $actionLabel"))
                    loadNotifications()
                }

                is Result.Error -> {
                    updateState {
                        copy(
                            isLoading = false,
                            error = "Failed to execute action"
                        )
                    }
                }
            }
        }
    }
}
