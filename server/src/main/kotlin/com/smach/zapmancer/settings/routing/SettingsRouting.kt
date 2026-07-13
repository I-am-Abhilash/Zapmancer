package com.smach.zapmancer.settings.routing

import com.smach.zapmancer.common.respondResult
import com.smach.zapmancer.core.common.dto.ToggleRequest
import com.smach.zapmancer.security.UserPrincipal
import com.smach.zapmancer.settings.domain.SettingsService
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
            /** GET /settings */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                call.respondResult(service.getSettings(principal.uid))
            }

            /** PUT /settings/2fa */
            put("/2fa") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                call.respondResult(service.toggle2fa(principal.uid, req.enabled))
            }

            /** PUT /settings/email-notifications */
            put("/email-notifications") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                call.respondResult(service.toggleEmailNotifications(principal.uid, req.enabled))
            }

            /** PUT /settings/client-mode */
            put("/client-mode") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<ToggleRequest>()
                call.respondResult(service.toggleClientMode(principal.uid, req.enabled))
            }
        }
    }
}
