package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.MessageRepository

class MarkConversationAsReadUseCase(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(conversationId: String): Result<Unit, DataError.Network> {
        return repository.markAsRead(conversationId)
    }
}
