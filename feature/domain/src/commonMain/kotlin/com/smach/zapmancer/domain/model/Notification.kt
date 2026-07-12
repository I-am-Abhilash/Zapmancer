package com.smach.zapmancer.domain.model

data class NotificationItem(
    val id: String,
    val type: NotificationType,
    val title: String,
    val description: String,
    val timestamp: String,
    val section: String,
    val codeSnippet: String? = null,
    val isItalic: Boolean = false,
    val actions: List<NotificationAction> = emptyList(),
    val quickReply: Boolean = false,
    val isRead: Boolean = false,
)

enum class NotificationType {
    MILESTONE,
    MESSAGE,
    ALERT,
    GENERAL,
    COLLABORATOR,
    UNKNOWN,
}

data class NotificationAction(
    val label: String,
    val isPrimary: Boolean = false,
    val isError: Boolean = false,
)
