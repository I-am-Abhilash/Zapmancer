package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.repository.MessageRepository

class GetConversationsUseCase(
    private val repository: MessageRepository
) {
    suspend operator fun invoke(): Result<List<ConversationItem>, DataError.Network> {
        return repository.getConversations()
    }
}
