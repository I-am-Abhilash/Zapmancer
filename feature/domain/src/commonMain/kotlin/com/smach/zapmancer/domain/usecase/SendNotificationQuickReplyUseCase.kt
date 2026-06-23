package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.NotificationRepository

class SendNotificationQuickReplyUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(
        notificationId: String,
        replyText: String,
    ): Result<Unit, DataError.Network> = repository.sendQuickReply(notificationId, replyText)
}
