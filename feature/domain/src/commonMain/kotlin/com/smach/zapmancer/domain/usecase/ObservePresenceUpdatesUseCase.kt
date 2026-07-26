package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObservePresenceUpdatesUseCase(
    private val repository: MessageRepository,
) {
    operator fun invoke(): Flow<Pair<String, Boolean>> = repository.observePresenceUpdates()
}
