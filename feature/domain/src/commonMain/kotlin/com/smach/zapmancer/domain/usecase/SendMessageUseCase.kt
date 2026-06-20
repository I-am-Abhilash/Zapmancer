package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.MessageRepository

class SendMessageUseCase(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(conversationId: String, text: String): Result<Unit, DataError.Network> {
        return repository.sendMessage(conversationId, text)
    }
}
