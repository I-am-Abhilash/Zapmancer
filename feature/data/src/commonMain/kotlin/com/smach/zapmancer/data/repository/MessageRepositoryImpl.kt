package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.Serializable

class MessageRepositoryImpl(
    private val client: HttpClient
) : MessageRepository {

    override suspend fun getConversations(): Result<List<ConversationItem>, DataError.Network> {
        return safeApiCall<List<ConversationItem>> {
            client.get("messages/conversations")
        }
    }

    override fun getMessages(conversationId: String): Flow<List<MessageItem>> = flow {
        while (true) {
            val result = safeApiCall<List<MessageItem>> {
                client.get("messages/conversations/$conversationId/messages")
            }
            if (result is Result.Success) {
                emit(result.data)
            }
            delay(5000) // Poll every 5 seconds for new messages in chat
        }
    }

    override suspend fun sendMessage(
        conversationId: String,
        text: String
    ): Result<Unit, DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("messages/conversations/$conversationId/send") {
                setBody(SendMessageRequest(text = text))
            }
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun markAsRead(conversationId: String): Result<Unit, DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("messages/conversations/$conversationId/read")
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }
}

@Serializable
private data class SendMessageRequest(val text: String)
