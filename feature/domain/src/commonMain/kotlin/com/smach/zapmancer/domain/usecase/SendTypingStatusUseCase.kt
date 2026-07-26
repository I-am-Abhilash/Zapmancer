package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.MessageRepository
import org.koin.core.annotation.Factory

@Factory
class SendTypingStatusUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String, isTyping: Boolean) {
        repository.sendTypingStatus(conversationId, isTyping)
    }
}
