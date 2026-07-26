package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.MessageRepository
import org.koin.core.annotation.Factory

@Factory
class MarkConversationAsReadUseCase(
    private val repository: MessageRepository,
) {
    suspend operator fun invoke(conversationId: String): Result<Unit, DataError.Network> = repository.markAsRead(conversationId)
}
