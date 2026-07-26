package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.NotificationRepository
import org.koin.core.annotation.Factory


@Factory
class ExecuteNotificationActionUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(
        notificationId: String,
        actionLabel: String,
    ): Result<Unit, DataError.Network> = repository.executeAction(notificationId, actionLabel)
}
