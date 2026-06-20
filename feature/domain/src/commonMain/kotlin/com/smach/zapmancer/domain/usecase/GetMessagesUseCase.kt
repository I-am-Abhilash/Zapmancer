package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val repository: MessageRepository
) {
    operator fun invoke(conversationId: String): Flow<List<MessageItem>> {
        return repository.getMessages(conversationId)
    }
}
