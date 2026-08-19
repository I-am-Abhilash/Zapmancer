package com.smach.zapmancer.settings.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.SettingsData
import com.smach.zapmancer.core.common.dto.ToggleRequest
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.settings.service.SettingsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * User Account Settings and Preferences routing module.
 */
fun Route.settingsRouting() {
    val service by inject<SettingsService>()

    authenticate("local-jwt") {
        route("/settings") {
            /**
             * Fetch user account settings and preferences
             *
             * Retrieves security flags (2FA enabled), email notification toggles, and client mode UI preferences for the authenticated user.
             *
             * @tags Settings & Preferences
             * @security BearerAuth
             * @response 200 Account settings preferences. [SettingsData]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.getSettings(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle two-factor authentication
             *
             * Enables or disables two-factor authentication enforcement on the user account.
             *
             * @tags Settings & Preferences
             * @security BearerAuth
             * @response 200 Two-factor setting updated. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            put("/2fa") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggle2fa(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle email notification preferences
             *
             * Enables or disables transactional and marketing email notifications.
             *
             * @tags Settings & Preferences
             * @security BearerAuth
             * @response 200 Email notifications setting updated. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            put("/email-notifications") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggleEmailNotifications(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle client mode workspace view
             *
             * Switches default workspace interface between Freelancer Mode and Client Hiring Mode.
             *
             * @tags Settings & Preferences
             * @security BearerAuth
             * @response 200 Client mode toggle updated. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            put("/client-mode") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggleClientMode(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}
