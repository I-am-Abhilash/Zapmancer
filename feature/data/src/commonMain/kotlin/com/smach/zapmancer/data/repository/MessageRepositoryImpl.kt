package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.data.repository.MessageRepositoryImpl.Companion.POLL_INTERVAL_MS
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.serialization.Serializable

class MessageRepositoryImpl(
    private val client: HttpClient,
) : MessageRepository {

    override suspend fun getConversations(): Result<List<ConversationItem>, DataError.Network> = safeApiCall<List<ConversationItem>> { client.get("messages/conversations") }

    /**
     * Polls the conversation endpoint every [POLL_INTERVAL_MS] while the collector
     * is active. Cancellation-aware — when the screen leaves the composition,
     * the upstream's coroutine is cancelled and the polling loop exits cleanly.
     */
    override fun getMessages(conversationId: String): Flow<List<MessageItem>> = flow {
        while (currentCoroutineContext().isActive) {
            val result = safeApiCall<List<MessageItem>> {
                client.get("messages/conversations/$conversationId/messages")
            }
            if (result is Result.Success) emit(result.data)
            delay(POLL_INTERVAL_MS)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun sendMessage(
        conversationId: String,
        text: String,
    ): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("messages/conversations/$conversationId/send") {
            setBody(SendMessageRequest(text = text))
        }
    }.toUnitResult()

    override suspend fun markAsRead(conversationId: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> { client.post("messages/conversations/$conversationId/read") }.toUnitResult()

    companion object {
        private const val POLL_INTERVAL_MS = 5_000L
    }
}

@Serializable
private data class SendMessageRequest(val text: String)
