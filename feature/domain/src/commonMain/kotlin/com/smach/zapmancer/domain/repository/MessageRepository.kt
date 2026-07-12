package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.model.MessageItem
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun getConversations(): Result<List<ConversationItem>, DataError.Network>
    fun getMessages(conversationId: String): Flow<Result<List<MessageItem>, DataError.Network>>
    suspend fun sendMessage(conversationId: String, text: String): Result<Unit, DataError.Network>
    suspend fun markAsRead(conversationId: String): Result<Unit, DataError.Network>
}
