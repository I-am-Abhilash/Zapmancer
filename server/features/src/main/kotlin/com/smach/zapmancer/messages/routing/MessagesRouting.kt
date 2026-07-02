package com.smach.zapmancer.messages.routing

import com.smach.zapmancer.common.ApiError
import com.smach.zapmancer.common.ApiResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.respondResult
import com.smach.zapmancer.feature.api.message.SendMessageRequest
import com.smach.zapmancer.messages.domain.MessagesService
import com.smach.zapmancer.security.UserPrincipal
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.messagesRouting() {
    val service by inject<MessagesService>()

    authenticate("local-jwt") {
        route("/messages/conversations") {
            /** GET /messages/conversations */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                call.respondResult(service.getConversations(principal.uid))
            }

            route("/{conversationId}") {
                /** GET /messages/conversations/{conversationId}/messages */
                get("/messages") {
                    val principal = call.principal<UserPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                    val convId = call.parameters["conversationId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                    when (val r = service.getMessages(convId, principal.uid)) {
                        is DomainResult.Success -> call.respond(ApiResponse(success = true, data = r.data))
                        is DomainResult.Error -> call.respond(r.code.httpStatusCode, ApiResponse<Unit>(false, error = ApiError(r.code.name, r.message ?: "")))
                    }
                }

                /** POST /messages/conversations/{conversationId}/send */
                post("/send") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(HttpStatusCode.Unauthorized)
                    val convId = call.parameters["conversationId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<SendMessageRequest>()
                    call.respondResult(service.sendMessage(convId, principal.uid, req.text))
                }

                /** POST /messages/conversations/{conversationId}/read */
                post("/read") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(HttpStatusCode.Unauthorized)
                    val convId = call.parameters["conversationId"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                    call.respondResult(service.markRead(convId, principal.uid))
                }
            }
        }
    }
}
