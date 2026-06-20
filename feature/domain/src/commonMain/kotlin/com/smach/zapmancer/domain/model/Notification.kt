package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val description: String,
    val timestamp: String,
    val section: String, // e.g., "Today", "Yesterday"
    val codeSnippet: String? = null,
    val isItalic: Boolean = false,
    val actions: List<NotificationAction> = emptyList(),
    val quickReply: Boolean = false
)

@Serializable
enum class NotificationType {
    MILESTONE, MESSAGE, ALERT, GENERAL, COLLABORATOR
}

@Serializable
data class NotificationAction(
    val label: String,
    val isPrimary: Boolean = false,
    val isError: Boolean = false
)
