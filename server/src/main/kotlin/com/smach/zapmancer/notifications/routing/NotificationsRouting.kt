package com.smach.zapmancer.notifications.routing

import com.smach.zapmancer.core.common.dto.ExecuteActionRequest
import com.smach.zapmancer.core.common.dto.SendQuickReplyRequest
import com.smach.zapmancer.core.common.respondResult
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.notifications.service.NotificationsService
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

fun Route.notificationsRouting() {
    val service by inject<NotificationsService>()

    authenticate("local-jwt") {
        route("/notifications") {
            /** GET /notifications */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                call.respondResult(service.getNotifications(principal.uid))
            }

            route("/{notificationId}") {
                /** POST /notifications/{notificationId}/action */
                post("/action") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<ExecuteActionRequest>()
                    call.respondResult(
                        service.executeAction(
                            notifId,
                            principal.uid,
                            req.actionLabel,
                        ),
                    )
                }

                /** POST /notifications/{notificationId}/reply */
                post("/reply") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<SendQuickReplyRequest>()
                    call.respondResult(
                        service.sendQuickReply(
                            notifId,
                            principal.uid,
                            req.replyText,
                        ),
                    )
                }
            }
        }
    }
}
