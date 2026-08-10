package com.smach.zapmancer.settings.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.SettingsData
import com.smach.zapmancer.core.common.dto.ToggleRequest
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

fun Route.settingsRouting() {
    val service by inject<SettingsService>()

    authenticate("local-jwt") {
        route("/settings") {
            /**
             * Retrieve authenticated user's account settings preferences.
             *
             * Responses:
             *   – 200 [ApiResponse<SettingsData>] Settings data.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *   – 404 [ApiResponse<Unit>] User settings not found.
             *
             * Tags: Settings
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val result = service.getSettings(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle Two-Factor Authentication setting.
             *
             * Request: [ToggleRequest] Target toggle status
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] 2FA status updated.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Settings
             */
            put("/2fa") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggle2fa(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle Email Notification preferences.
             *
             * Request: [ToggleRequest] Target toggle status
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Email notification status updated.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Settings
             */
            put("/email-notifications") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggleEmailNotifications(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Toggle Client Mode UI preference.
             *
             * Request: [ToggleRequest] Target toggle status
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Client mode status updated.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Settings
             */
            put("/client-mode") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                val result = service.toggleClientMode(principal.uid, req.enabled)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}


