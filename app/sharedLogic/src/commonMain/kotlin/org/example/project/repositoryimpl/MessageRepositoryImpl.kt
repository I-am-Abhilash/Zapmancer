package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.dto.SendMessageRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.NetworkConstants
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.core.network.session.SessionManager
import com.smach.zapmancer.domain.model.ConversationItem
import com.smach.zapmancer.domain.model.MessageItem
import com.smach.zapmancer.domain.model.MessageStatus
import com.smach.zapmancer.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.milliseconds
import com.smach.zapmancer.core.common.dto.ConversationItem as ConversationItemDto
import com.smach.zapmancer.core.common.dto.MessageItem as MessageItemDto
@Single(binds = [MessageRepository::class])
class MessageRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) : MessageRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val wsEventFlow = MutableSharedFlow<ChatFrame.ServerToClient>(extraBufferCapacity = 128)
    private var webSocketSession: DefaultClientWebSocketSession? = null

    init {
        scope.launch {
            connectWebSocketWithRetry()
        }
    }

    private suspend fun connectWebSocketWithRetry() {
        while (currentCoroutineContext().isActive) {
            val token = sessionManager.getAccessToken()
            if (token.isNullOrEmpty()) {
                delay(3000.milliseconds)
                continue
            }

            val wsUrl = NetworkConstants.BASE_URL
                .replace("http://", "ws://")
                .replace("https://", "wss://")
                .plus("messages/chat?token=$token")

            try {
                val session = client.webSocketSession {
                    url(wsUrl)
                }
                webSocketSession = session

                for (frame in session.incoming) {
                    if (frame is Frame.Text) {
                        try {
                            val text = frame.readText()
                            val chatFrame = Json.decodeFromString<ChatFrame.ServerToClient>(text)
                            wsEventFlow.emit(chatFrame)
                        } catch (_: Exception) {}
                    }
                }
            } catch (_: Exception) {
            } finally {
                webSocketSession = null
                delay(5000.milliseconds) // Retry delay
            }
        }
    }

    override suspend fun getConversations(): Result<List<ConversationItem>, DataError.Network> = safeApiCall<List<ConversationItemDto>> { client.get("messages/conversations") }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            is Result.Error -> result
        }
    }

    override fun getMessages(conversationId: String): Flow<Result<List<MessageItem>, DataError.Network>> = flow {
        val initialResult = safeApiCall<List<MessageItemDto>> {
            client.get("messages/conversations/$conversationId/messages")
        }.let { res ->
            when (res) {
                is Result.Success -> Result.Success(res.data.map { it.toDomain() })
                is Result.Error -> res
            }
        }

        if (initialResult is Result.Error) {
            emit(initialResult)
            return@flow
        }

        val messageList = (initialResult as Result.Success).data.toMutableList()
        emit(Result.Success(messageList.toList()))

        wsEventFlow.collect { frame ->
            when (frame) {
                is ChatFrame.ServerToClient.NewMessage -> {
                    if (frame.conversationId == conversationId) {
                        val newMsg = frame.message.toDomain()
                        if (messageList.none { it.id == newMsg.id }) {
                            messageList.add(newMsg)
                            emit(Result.Success(messageList.toList()))
                        }
                    }
                }

                is ChatFrame.ServerToClient.MessageStatusUpdate -> {
                    if (frame.conversationId == conversationId) {
                        val statusVal = when (frame.status) {
                            "DELIVERED" -> MessageStatus.DELIVERED
                            "READ" -> MessageStatus.READ
                            else -> MessageStatus.SENT
                        }
                        var modified = false
                        for (i in messageList.indices) {
                            val msg = messageList[i]
                            if (frame.messageId.isEmpty() || msg.id == frame.messageId) {
                                if (msg.status != statusVal) {
                                    messageList[i] = msg.copy(status = statusVal)
                                    modified = true
                                }
                            }
                        }
                        if (modified) {
                            emit(Result.Success(messageList.toList()))
                        }
                    }
                }

                else -> {}
            }
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

    override fun observePresence(conversationId: String): Flow<Boolean> = flow {
        wsEventFlow.collect { frame ->
            if (frame is ChatFrame.ServerToClient.PresenceUpdate && frame.conversationId == conversationId) {
                emit(frame.isOnline)
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun observePresenceUpdates(): Flow<Pair<String, Boolean>> = flow {
        wsEventFlow.collect { frame ->
            if (frame is ChatFrame.ServerToClient.PresenceUpdate) {
                emit(Pair(frame.conversationId, frame.isOnline))
            }
        }
    }.flowOn(Dispatchers.Default)

    override fun observeTyping(conversationId: String): Flow<Boolean> = flow {
        wsEventFlow.collect { frame ->
            if (frame is ChatFrame.ServerToClient.TypingUpdate && frame.conversationId == conversationId) {
                emit(frame.isTyping)
            }
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun sendTypingStatus(conversationId: String, isTyping: Boolean) {
        val session = webSocketSession
        if (session != null) {
            try {
                val frame = ChatFrame.ClientToServer.Typing(conversationId, isTyping)
                session.send(Json.encodeToString<ChatFrame.ClientToServer>(frame))
            } catch (_: Exception) {}
        }
    }
}

private fun ConversationItemDto.toDomain(): ConversationItem = ConversationItem(
    id = id,
    name = name,
    avatarUrl = avatarUrl.orEmpty(),
    lastMessage = lastMessage,
    timestamp = timestamp,
    isUnread = isUnread,
    isOnline = isOnline,
)

private fun MessageItemDto.toDomain(): MessageItem = MessageItem(
    id = id,
    text = text,
    timestamp = timestamp,
    isFromMe = isFromMe,
    status = when (status) {
        "DELIVERED" -> MessageStatus.DELIVERED
        "READ" -> MessageStatus.READ
        else -> MessageStatus.SENT
    },
    avatarUrl = avatarUrl,
)
