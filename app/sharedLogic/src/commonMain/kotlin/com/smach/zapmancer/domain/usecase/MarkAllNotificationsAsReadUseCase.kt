package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.NotificationRepository
import org.koin.core.annotation.Factory

@Factory
class MarkAllNotificationsAsReadUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<Unit, DataError.Network> = repository.markAllAsRead()
}
