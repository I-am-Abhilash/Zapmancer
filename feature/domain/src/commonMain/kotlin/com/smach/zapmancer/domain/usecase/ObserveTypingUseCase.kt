package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class ObserveTypingUseCase(
    private val repository: MessageRepository,
) {
    operator fun invoke(conversationId: String): Flow<Boolean> = repository.observeTyping(conversationId)
}
