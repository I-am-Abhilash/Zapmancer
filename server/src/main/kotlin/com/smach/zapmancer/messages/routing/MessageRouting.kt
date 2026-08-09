package com.smach.zapmancer.messages.routing

import com.smach.zapmancer.core.common.ApiResponse
import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.SendMessageRequest
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.messages.service.ConnectionManager
import com.smach.zapmancer.messages.service.MessageService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

fun Route.messageRouting() {
    val service by inject<MessageService>()
    val connectionManager by inject<ConnectionManager>()

    authenticate("local-jwt") {
        webSocket("/messages/chat") {
            val principal = call.principal<UserPrincipal>() ?: return@webSocket close(
                CloseReason(CloseReason.Codes.VIOLATED_POLICY, "Unauthorized"),
            )
            val userId = principal.uid

            val isFirstConnection = connectionManager.registerSession(userId, this)
            if (isFirstConnection) {
                val conversations = service.getConversations(userId)
                conversations.forEach { conversation ->
                    val otherParticipants = service.getConversationParticipants(conversation.id)
                    if (otherParticipants != null) {
                        val otherUserId = if (userId == otherParticipants.first) otherParticipants.second else otherParticipants.first
                        connectionManager.sendToUser(
                            otherUserId,
                            ChatFrame.ServerToClient.PresenceUpdate(conversation.id, isOnline = true),
                        )
                    }
                }
            }

            // Inform client of current online status of all their conversations
            val conversations = service.getConversations(userId)
            conversations.forEach { conversation ->
                val otherParticipants = service.getConversationParticipants(conversation.id)
                if (otherParticipants != null) {
                    val otherUserId = if (userId == otherParticipants.first) otherParticipants.second else otherParticipants.first
                    val otherOnline = connectionManager.isUserOnline(otherUserId)
                    if (otherOnline) {
                        try {
                            send(
                                Json.encodeToString<ChatFrame.ServerToClient>(
                                    ChatFrame.ServerToClient.PresenceUpdate(conversation.id, isOnline = true),
                                ),
                            )
                        } catch (_: Exception) {}
                    }
                }
            }

            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) {
                        val text = frame.readText()
                        try {
                            val clientFrame = Json.decodeFromString<ChatFrame.ClientToServer>(text)
                            when (clientFrame) {
                                is ChatFrame.ClientToServer.Typing -> {
                                    val otherParticipants = service.getConversationParticipants(clientFrame.conversationId)
                                    if (otherParticipants != null) {
                                        val otherUserId = if (userId == otherParticipants.first) otherParticipants.second else otherParticipants.first
                                        connectionManager.sendToUser(
                                            otherUserId,
                                            ChatFrame.ServerToClient.TypingUpdate(clientFrame.conversationId, clientFrame.isTyping),
                                        )
                                    }
                                }
                            }
                        } catch (e: Exception) {
                            send(
                                Json.encodeToString<ChatFrame.ServerToClient>(
                                    ChatFrame.ServerToClient.Error("Malformed WebSocket payload: ${e.message}"),
                                ),
                            )
                        }
                    }
                }
            } catch (_: Exception) {
            } finally {
                val isFullyOffline = connectionManager.deregisterSession(userId, this)
                if (isFullyOffline) {
                    val userConvs = service.getConversations(userId)
                    userConvs.forEach { conversation ->
                        val otherParticipants = service.getConversationParticipants(conversation.id)
                        if (otherParticipants != null) {
                            val otherUserId = if (userId == otherParticipants.first) otherParticipants.second else otherParticipants.first
                            connectionManager.sendToUser(
                                otherUserId,
                                ChatFrame.ServerToClient.PresenceUpdate(conversation.id, isOnline = false),
                            )
                        }
                    }
                }
            }
        }

        route("/messages/conversations") {
            /** GET /messages/conversations */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val conversations = service.getConversations(principal.uid)
                call.respond(ApiResponse(success = true, data = conversations))
            }

            route("/{conversationId}") {
                /** GET /messages/conversations/{conversationId}/messages */
                get("/messages") {
                    val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val convId = call.parameters["conversationId"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                    )
                    val messages = service.getMessages(convId, principal.uid)
                    call.respond(ApiResponse(success = true, data = messages))
                }

                /** POST /messages/conversations/{conversationId}/send */
                post("/send") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val convId = call.parameters["conversationId"] ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                    )
                    val req = call.receive<SendMessageRequest>()
                    val result = service.sendMessage(convId, principal.uid, req.text)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /** POST /messages/conversations/{conversationId}/read */
                post("/read") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val convId = call.parameters["conversationId"] ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                    )
                    val result = service.markRead(convId, principal.uid)
                    call.respond(ApiResponse(success = true, data = result))
                }
            }
        }
    }
}

