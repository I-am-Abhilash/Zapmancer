package com.smach.zapmancer.features.alerts.state

import com.smach.zapmancer.domain.model.NotificationItem

data class NotificationUiState(
    val notifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false,
    val replyDrafts: Map<String, String> = emptyMap(),
    val error: String? = null,
)
