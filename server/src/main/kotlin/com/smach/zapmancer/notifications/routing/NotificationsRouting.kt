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

/**
 * Notifications and Actionable Alerts routing module.
 */
fun Route.notificationsRouting() {
    val service by inject<NotificationsService>()

    authenticate("local-jwt") {
        route("/notifications") {
            /**
             * Fetch user notifications feed
             *
             * Retrieves the reverse-chronological notifications feed for the authenticated user.
             *
             * @tags Notifications
             * @security BearerAuth
             * @response 200 List of user notification items. [List<NotificationItem>]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Mark all notifications as read
             *
             * Marks all unread notifications in the user feed as read.
             *
             * @tags Notifications
             * @security BearerAuth
             * @response 200 All notifications marked read. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
                 * Mark notification as read
                 *
                 * Marks a specific notification item as read by its unique integer identifier.
                 *
                 * @tags Notifications
                 * @security BearerAuth
                 * @path notificationId The integer notification identifier.
                 * @response 200 Notification marked as read. [CommonResponse]
                 * @response 401 Missing or invalid authentication token. [ApiError]
                 * @response 404 Notification not found. [ApiError]
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
                 * Execute notification interactive action
                 *
                 * Executes an inline contextual action (e.g. Accept, Review, Reject) on a notification item.
                 *
                 * @tags Notifications
                 * @security BearerAuth
                 * @path notificationId The integer notification identifier.
                 * @response 200 Action executed successfully. [CommonResponse]
                 * @response 401 Missing or invalid authentication token. [ApiError]
                 * @response 404 Notification not found. [ApiError]
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
                 * Send quick reply from notification
                 *
                 * Dispatches a quick reply message in response to a contextual message or notification prompt.
                 *
                 * @tags Notifications
                 * @security BearerAuth
                 * @path notificationId The integer notification identifier.
                 * @response 200 Quick reply dispatched. [CommonResponse]
                 * @response 401 Missing or invalid authentication token. [ApiError]
                 * @response 404 Notification not found. [ApiError]
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
