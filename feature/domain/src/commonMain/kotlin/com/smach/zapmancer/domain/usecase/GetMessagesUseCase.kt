package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesUseCase(
    private val repository: MessageRepository,
) {
    operator fun invoke(conversationId: String): Flow<Result<List<MessageItem>, DataError.Network>> = repository.getMessages(conversationId)
}
