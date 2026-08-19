package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.NotificationRepository
import org.koin.core.annotation.Factory

@Factory
class MarkNotificationAsReadUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(notificationId: String): Result<Unit, DataError.Network> = repository.markAsRead(notificationId)
}
