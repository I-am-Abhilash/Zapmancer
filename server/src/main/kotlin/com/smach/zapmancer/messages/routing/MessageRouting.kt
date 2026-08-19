package com.smach.zapmancer.messages.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.AttachmentUploadResponse
import com.smach.zapmancer.core.common.dto.ChatFrame
import com.smach.zapmancer.core.common.dto.ConversationItem
import com.smach.zapmancer.core.common.dto.MessageItem
import com.smach.zapmancer.core.common.dto.SendMessageRequest
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.messages.service.ConnectionManager
import com.smach.zapmancer.messages.service.MessageService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import io.ktor.server.websocket.webSocket
import io.ktor.utils.io.readRemaining
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json
import org.koin.ktor.ext.inject

/**
 * Real-time WebSocket and REST Direct Messaging routing module.
 */
fun Route.messageRouting() {
    val service by inject<MessageService>()
    val connectionManager by inject<ConnectionManager>()

    authenticate("local-jwt") {
        /**
         * Real-time bidirectional chat WebSocket
         *
         * Establishes persistent WebSocket connection for real-time instant messaging, typing presence, read receipts, and live emoji reactions.
         *
         * @tags Direct Messaging
         * @security BearerAuth
         */
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
                            ChatFrame.ServerToClient.PresenceUpdate(conversation.id, userId, isOnline = true),
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
                                    ChatFrame.ServerToClient.PresenceUpdate(conversation.id, otherUserId, isOnline = true),
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
                                is ChatFrame.ClientToServer.SendMessage -> {
                                    service.sendMessage(
                                        conversationId = clientFrame.conversationId,
                                        senderId = userId,
                                        text = clientFrame.text,
                                        attachmentUrl = clientFrame.attachmentUrl,
                                        attachmentType = clientFrame.attachmentType,
                                        attachmentName = clientFrame.attachmentName,
                                        attachmentSizeBytes = clientFrame.attachmentSizeBytes,
                                        replyToMessageId = clientFrame.replyToMessageId,
                                    )
                                }

                                is ChatFrame.ClientToServer.Typing -> {
                                    service.handleTyping(clientFrame.conversationId, userId, clientFrame.isTyping)
                                }

                                is ChatFrame.ClientToServer.MarkRead -> {
                                    service.markMessageRead(clientFrame.conversationId, userId, clientFrame.messageId)
                                }

                                is ChatFrame.ClientToServer.React -> {
                                    service.handleReact(clientFrame.conversationId, userId, clientFrame.messageId, clientFrame.emoji)
                                }

                                is ChatFrame.ClientToServer.EditMessage -> {
                                    service.handleEditMessage(clientFrame.conversationId, userId, clientFrame.messageId, clientFrame.newText)
                                }

                                is ChatFrame.ClientToServer.DeleteMessage -> {
                                    service.handleDeleteMessage(clientFrame.conversationId, userId, clientFrame.messageId)
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
                                ChatFrame.ServerToClient.PresenceUpdate(conversation.id, userId, isOnline = false),
                            )
                        }
                    }
                }
            }
        }

        route("/messages") {
            /**
             * Upload chat file attachment
             *
             * Uploads a multi-part binary file (image, PDF, archive, audio) to secure cloud storage and returns the CDN public URL.
             *
             * @tags Direct Messaging
             * @security BearerAuth
             * @response 200 File uploaded successfully. [AttachmentUploadResponse]
             * @response 400 Missing file binary payload. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/attachment") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val multipart = call.receiveMultipart()
                var fileBytes: ByteArray? = null
                var fileName = "attachment.bin"
                var contentType = "application/octet-stream"

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem) {
                        fileName = part.originalFileName ?: "attachment.bin"
                        contentType = part.contentType?.toString() ?: "application/octet-stream"
                        fileBytes = part.provider().readRemaining().readByteArray()
                    }
                    part.dispose()
                }

                if (fileBytes == null) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing file payload")),
                    )
                }

                val result = service.uploadAttachment(principal.uid, fileName, fileBytes!!, contentType)
                call.respond(ApiResponse(success = true, data = result))
            }

            route("/conversations") {
                /**
                 * Fetch user active conversations
                 *
                 * Retrieves a list of all active 1-on-1 message conversations for the authenticated user, including unread counts and last message previews.
                 *
                 * @tags Direct Messaging
                 * @security BearerAuth
                 * @response 200 List of conversation items. [List<ConversationItem>]
                 * @response 401 Missing or invalid authentication token. [ApiError]
                 */
                get {
                    val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val conversations = service.getConversations(principal.uid)
                    call.respond(ApiResponse(success = true, data = conversations))
                }

                route("/{conversationId}") {
                    /**
                     * Fetch conversation message thread
                     *
                     * Retrieves paginated historical message items for a specific conversation thread.
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @query limit Maximum number of historical messages to retrieve.
                     * @response 200 List of thread messages. [List<MessageItem>]
                     * @response 400 Missing conversation id parameter. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    get("/messages") {
                        val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                            HttpStatusCode.Unauthorized,
                        )
                        val convId = call.parameters["conversationId"] ?: return@get call.respond(
                            HttpStatusCode.BadRequest,
                        )
                        val limit = call.request.queryParameters["limit"]?.toIntOrNull() ?: 50
                        val messages = service.getMessages(convId, principal.uid, limit)
                        call.respond(ApiResponse(success = true, data = messages))
                    }

                    /**
                     * Send message via REST API
                     *
                     * Sends a text or attachment message to a conversation thread via REST (alternative to WebSocket).
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @response 200 Message dispatched successfully. [MessageItem]
                     * @response 400 Missing required parameters or empty message. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    post("/send") {
                        val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                            HttpStatusCode.Unauthorized,
                        )
                        val convId = call.parameters["conversationId"] ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                        )
                        val req = call.receive<SendMessageRequest>()
                        val result = service.sendMessage(
                            conversationId = convId,
                            senderId = principal.uid,
                            text = req.text,
                            attachmentUrl = req.attachmentUrl,
                            attachmentType = req.attachmentType,
                            attachmentName = req.attachmentName,
                            attachmentSizeBytes = req.attachmentSizeBytes,
                            replyToMessageId = req.replyToMessageId,
                        )
                        call.respond(ApiResponse(success = true, data = result))
                    }

                    /**
                     * Mark conversation messages as read
                     *
                     * Marks all messages in the conversation thread as read for the authenticated user.
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @response 200 All messages marked as read. [CommonResponse]
                     * @response 400 Missing conversation id. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    post("/read") {
                        val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                            HttpStatusCode.Unauthorized,
                        )
                        val convId = call.parameters["conversationId"] ?: return@post call.respond(
                            HttpStatusCode.BadRequest,
                        )
                        val result = service.markAllRead(convId, principal.uid)
                        call.respond(ApiResponse(success = true, data = result))
                    }

                    /**
                     * Toggle emoji reaction on message
                     *
                     * Adds or removes an emoji reaction on a specific chat message.
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @path messageId The unique message identifier.
                     * @query emoji Unicode emoji character string.
                     * @response 200 Reaction updated successfully. [CommonResponse]
                     * @response 400 Missing conversation or message id. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    post("/messages/{messageId}/react") {
                        val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                            HttpStatusCode.Unauthorized,
                        )
                        val convId = call.parameters["conversationId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                        val messageId = call.parameters["messageId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                        val emoji = call.request.queryParameters["emoji"] ?: "👍"

                        service.handleReact(convId, principal.uid, messageId, emoji)
                        call.respond(ApiResponse(success = true, data = CommonResponse(success = true, message = "Reaction updated.")))
                    }

                    /**
                     * Edit sent message text
                     *
                     * Edits the text content of a previously sent chat message. Restricted to the original sender.
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @path messageId The unique message identifier.
                     * @response 200 Message updated successfully. [CommonResponse]
                     * @response 400 Missing conversation or message id. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    put("/messages/{messageId}") {
                        val principal = call.principal<UserPrincipal>() ?: return@put call.respond(HttpStatusCode.Unauthorized)
                        val convId = call.parameters["conversationId"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val messageId = call.parameters["messageId"] ?: return@put call.respond(HttpStatusCode.BadRequest)
                        val req = call.receive<SendMessageRequest>()

                        service.handleEditMessage(convId, principal.uid, messageId, req.text)
                        call.respond(ApiResponse(success = true, data = CommonResponse(success = true, message = "Message edited.")))
                    }

                    /**
                     * Delete sent message
                     *
                     * Soft-deletes or removes a sent message from the conversation thread. Restricted to the original sender.
                     *
                     * @tags Direct Messaging
                     * @security BearerAuth
                     * @path conversationId The unique conversation identifier.
                     * @path messageId The unique message identifier.
                     * @response 200 Message deleted successfully. [CommonResponse]
                     * @response 400 Missing conversation or message id. [ApiError]
                     * @response 401 Missing or invalid authentication token. [ApiError]
                     */
                    delete("/messages/{messageId}") {
                        val principal = call.principal<UserPrincipal>() ?: return@delete call.respond(HttpStatusCode.Unauthorized)
                        val convId = call.parameters["conversationId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
                        val messageId = call.parameters["messageId"] ?: return@delete call.respond(HttpStatusCode.BadRequest)

                        service.handleDeleteMessage(convId, principal.uid, messageId)
                        call.respond(ApiResponse(success = true, data = CommonResponse(success = true, message = "Message deleted.")))
                    }
                }
            }
        }
    }
}
