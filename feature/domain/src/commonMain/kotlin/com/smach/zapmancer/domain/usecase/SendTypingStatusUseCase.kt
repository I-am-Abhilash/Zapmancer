package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.MessageRepository

class SendTypingStatusUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String, isTyping: Boolean) {
        repository.sendTypingStatus(conversationId, isTyping)
    }
}
