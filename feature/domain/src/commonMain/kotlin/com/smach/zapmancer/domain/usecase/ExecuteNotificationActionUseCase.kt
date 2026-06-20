package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.NotificationRepository

class ExecuteNotificationActionUseCase(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String, actionLabel: String): Result<Unit, DataError.Network> {
        return repository.executeAction(notificationId, actionLabel)
    }
}
