package com.smach.zapmancer.notifications.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.ExecuteActionRequest
import com.smach.zapmancer.core.common.dto.NotificationItem
import com.smach.zapmancer.core.common.dto.SendQuickReplyRequest
import com.smach.zapmancer.core.network.ktor.ApiResponse
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
            /**
             * Retrieve user's system notifications feed.
             *
             * Responses:
             *   – 200 [ApiResponse<List<NotificationItem>>] Notification items list.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Notifications
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val result = service.getNotifications(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            route("/{notificationId}") {
                /**
                 * Execute an action on a specific notification item.
                 *
                 * Path: notificationId [Int] Target notification ID
                 * Request: [ExecuteActionRequest] Action details
                 *
                 * Responses:
                 *   – 200 [ApiResponse<CommonResponse>] Action executed successfully.
                 *   – 400 [ApiResponse<Unit>] Invalid notification ID.
                 *
                 * Tags: Notifications
                 */
                post("/action") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<ExecuteActionRequest>()
                    val result = service.executeAction(
                        notifId,
                        principal.uid,
                        req.actionLabel,
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Submit a quick inline reply to a notification.
                 *
                 * Path: notificationId [Int] Target notification ID
                 * Request: [SendQuickReplyRequest] Reply text payload
                 *
                 * Responses:
                 *   – 200 [ApiResponse<CommonResponse>] Quick reply sent.
                 *   – 400 [ApiResponse<Unit>] Invalid notification ID.
                 *
                 * Tags: Notifications
                 */
                post("/reply") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(HttpStatusCode.BadRequest)
                    val req = call.receive<SendQuickReplyRequest>()
                    val result = service.sendQuickReply(
                        notifId,
                        principal.uid,
                        req.replyText,
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }
            }
        }
    }
}


