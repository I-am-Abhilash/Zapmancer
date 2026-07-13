package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class ObservePresenceUseCase(
    private val repository: MessageRepository,
) {
    operator fun invoke(conversationId: String): Flow<Boolean> = repository.observePresence(conversationId)
}
