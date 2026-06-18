package com.smach.zapmancer.features.alerts.state

data class NotificationUiState(
    val notifications: List<NotificationItem> = sampleNotifications,
    val isLoading: Boolean = false
)

val sampleNotifications = listOf(
    NotificationItem(
        id = "1",
        type = NotificationType.MILESTONE,
        title = "Neural Mesh Deployment",
        description = "Automated deployment of the v2.4.1-alpha build was successful on the production cluster.",
        timestamp = "2m ago",
        section = "Today",
        actions = listOf(
            NotificationAction("View Logs", isPrimary = true),
            NotificationAction("Dismiss")
        )
    ),
    NotificationItem(
        id = "2",
        type = NotificationType.MESSAGE,
        title = "Message from Sarah Connor",
        description = "The latency on the North-East edge node has stabilized. Should we increase the load distribution?",
        timestamp = "1h ago",
        section = "Today",
        isItalic = true,
        quickReply = true
    ),
    NotificationItem(
        id = "3",
        type = NotificationType.ALERT,
        title = "Database Connection Spike",
        description = "Unauthorized access attempts detected from IP 192.168.1.104. Security protocols initiated.",
        timestamp = "4h ago",
        section = "Today",
        actions = listOf(
            NotificationAction("Block IP", isPrimary = true, isError = true),
            NotificationAction("Investigate")
        )
    ),
    NotificationItem(
        id = "4",
        type = NotificationType.GENERAL,
        title = "Weekly Backup Complete",
        description = "All system partitions have been mirrored to the secure vault. Integrity check: 100%.",
        timestamp = "1d ago",
        section = "Yesterday"
    ),
    NotificationItem(
        id = "5",
        type = NotificationType.COLLABORATOR,
        title = "New Collaborator Joined",
        description = "David Chen was added to the \"Project Phoenix\" team by Admin.",
        timestamp = "1d ago",
        section = "Yesterday"
    )
)


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

enum class NotificationType {
    MILESTONE, MESSAGE, ALERT, GENERAL, COLLABORATOR
}

data class NotificationAction(
    val label: String,
    val isPrimary: Boolean = false,
    val isError: Boolean = false
)
