package com.smach.zapmancer.notifications.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.core.common.dto.NotificationItem
import com.smach.zapmancer.notifications.data.NotificationsRepository

class NotificationsService(private val repository: NotificationsRepository) {

    suspend fun getNotifications(userId: String): DomainResult<List<NotificationItem>> = DomainResult.Success(repository.getNotifications(userId))

    suspend fun executeAction(
        notificationId: Int,
        userId: String,
        actionLabel: String,
    ): DomainResult<CommonResponse> {
        // Action execution logic (e.g., trigger invoice, approve proposal) goes here.
        // For now we acknowledge the action and return success.
        return DomainResult.Success(CommonResponse(success = true, message = "Action executed successfully."))
    }

    suspend fun sendQuickReply(
        notificationId: Int,
        userId: String,
        replyText: String,
    ): DomainResult<CommonResponse> {
        // Quick reply handling (e.g., dispatch to messages service) goes here.
        return DomainResult.Success(CommonResponse(success = true, message = "Quick reply sent."))
    }
}
