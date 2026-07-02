package com.smach.zapmancer.notifications.data

import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.NotificationActionsTable
import com.smach.zapmancer.database.NotificationsTable
import com.smach.zapmancer.core.common.dto.*
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.selectAll

class NotificationsRepository {

    suspend fun getNotifications(userId: String): List<NotificationItem> = dbQuery {
        NotificationsTable.selectAll()
            .where { NotificationsTable.userId eq userId }
            .orderBy(NotificationsTable.createdAt, SortOrder.DESC)
            .map { row ->
                val notifId = row[NotificationsTable.id]
                val actions = NotificationActionsTable.selectAll()
                    .where { NotificationActionsTable.notificationId eq notifId }
                    .map { a ->
                        NotificationAction(
                            label = a[NotificationActionsTable.label],
                            isPrimary = a[NotificationActionsTable.isPrimary],
                            isError = a[NotificationActionsTable.isError]
                        )
                    }
                NotificationItem(
                    id = notifId,
                    type = row[NotificationsTable.type],
                    title = row[NotificationsTable.title],
                    description = row[NotificationsTable.description],
                    timestamp = row[NotificationsTable.timestamp],
                    section = row[NotificationsTable.section],
                    codeSnippet = row[NotificationsTable.codeSnippet],
                    isItalic = row[NotificationsTable.isItalic],
                    actions = actions,
                    quickReply = row[NotificationsTable.quickReply]
                )
            }
    }
}
