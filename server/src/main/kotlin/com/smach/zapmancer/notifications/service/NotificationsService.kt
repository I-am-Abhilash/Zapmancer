package com.smach.zapmancer.notifications.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.NotificationItem
import com.smach.zapmancer.notifications.repository.NotificationsRepository
import org.koin.core.annotation.Single


@Single
class NotificationsService(private val repository: NotificationsRepository) {

    suspend fun getNotifications(userId: String): List<NotificationItem> = repository.getNotifications(userId)

    suspend fun executeAction(
        notificationId: Int,
        userId: String,
        actionLabel: String,
    ): CommonResponse {
        // Action execution logic (e.g., trigger invoice, approve proposal) goes here.
        // For now we acknowledge the action and return success.
        return CommonResponse(
            success = true,
            message = "Action executed successfully.",
        )
    }

    suspend fun sendQuickReply(
        notificationId: Int,
        userId: String,
        replyText: String,
    ): CommonResponse {
        // Quick reply handling (e.g., dispatch to messages service) goes here.
        return CommonResponse(success = true, message = "Quick reply sent.")
    }
}

