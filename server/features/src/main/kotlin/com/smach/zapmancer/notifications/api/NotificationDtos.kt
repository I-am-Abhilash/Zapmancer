package com.smach.zapmancer.notifications.api

import kotlinx.serialization.Serializable

@Serializable
data class NotificationItem(
    val id: Int,
    /** MILESTONE | MESSAGE | ALERT | GENERAL | COLLABORATOR */
    val type: String,
    val title: String,
    val description: String,
    val timestamp: String,
    val section: String,
    val codeSnippet: String?,
    val isItalic: Boolean,
    val actions: List<NotificationAction>,
    val quickReply: Boolean,
)

@Serializable
data class NotificationAction(
    val label: String,
    val isPrimary: Boolean,
    val isError: Boolean,
)

@Serializable
data class ExecuteActionRequest(val actionLabel: String)

@Serializable
data class SendQuickReplyRequest(val replyText: String)
