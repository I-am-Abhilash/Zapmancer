package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.NotificationItem
import com.smach.zapmancer.domain.repository.NotificationRepository
import org.koin.core.annotation.Factory

@Factory
class GetNotificationsUseCase(
    private val repository: NotificationRepository,
) {
    suspend operator fun invoke(): Result<List<NotificationItem>, DataError.Network> = repository.getNotifications()
}
