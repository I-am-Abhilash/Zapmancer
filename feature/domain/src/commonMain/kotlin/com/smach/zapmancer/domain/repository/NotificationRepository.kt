package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.NotificationItem

interface NotificationRepository {
    suspend fun getNotifications(): Result<List<NotificationItem>, DataError.Network>
    suspend fun executeAction(notificationId: String, actionLabel: String): Result<Unit, DataError.Network>
    suspend fun sendQuickReply(notificationId: String, replyText: String): Result<Unit, DataError.Network>
}
