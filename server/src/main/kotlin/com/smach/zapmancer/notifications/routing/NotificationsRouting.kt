package com.smach.zapmancer.notifications.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.ExecuteActionRequest
import com.smach.zapmancer.core.common.dto.NotificationItem
import com.smach.zapmancer.core.common.dto.SendQuickReplyRequest
import com.smach.zapmancer.core.network.ktor.ApiError
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
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.getNotifications(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Mark all notifications as read for the authenticated user.
             */
            post("/read-all") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.markAllAsRead(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            route("/{notificationId}") {
                /**
                 * Mark a specific notification as read.
                 */
                post("/read") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(
                            HttpStatusCode.NotFound,
                            ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Notification not found")),
                        )
                    val result = service.markAsRead(notifId, principal.uid)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Execute an action on a specific notification item.
                 */
                post("/action") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(
                            HttpStatusCode.NotFound,
                            ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Notification not found")),
                        )
                    val req = call.receive<ExecuteActionRequest>()
                    val result = service.executeAction(
                        notifId,
                        principal.uid,
                        req.actionLabel,
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Quick reply to a notification.
                 */
                post("/reply") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val notifId = call.parameters["notificationId"]?.toIntOrNull()
                        ?: return@post call.respond(
                            HttpStatusCode.NotFound,
                            ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Notification not found")),
                        )
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
