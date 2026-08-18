package com.smach.zapmancer.notifications.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
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
        if (actionLabel.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Action label cannot be blank.")
        }

        // Auto-mark notification as read upon action execution
        repository.markAsRead(notificationId, userId)

        val normalizedAction = actionLabel.trim().uppercase()
        val message = when (normalizedAction) {
            "VIEW_CONTRACT" -> "Contract review opened."
            "ACCEPT_OFFER" -> "Offer accepted successfully."
            "DECLINE_OFFER" -> "Offer declined."
            "DISMISS" -> "Notification dismissed."
            else -> "Action '$actionLabel' executed successfully."
        }

        return CommonResponse(
            success = true,
            message = message,
        )
    }

    suspend fun sendQuickReply(
        notificationId: Int,
        userId: String,
        replyText: String,
    ): CommonResponse {
        if (replyText.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Reply text cannot be blank.")
        }
        // Auto-mark notification as read upon replying
        repository.markAsRead(notificationId, userId)
        return CommonResponse(success = true, message = "Quick reply sent.")
    }

    suspend fun markAsRead(notificationId: Int, userId: String): CommonResponse {
        val updated = repository.markAsRead(notificationId, userId)
        return CommonResponse(success = updated, message = if (updated) "Notification marked as read." else "Notification not found.")
    }

    suspend fun markAllAsRead(userId: String): CommonResponse {
        repository.markAllAsRead(userId)
        return CommonResponse(success = true, message = "All notifications marked as read.")
    }
}
