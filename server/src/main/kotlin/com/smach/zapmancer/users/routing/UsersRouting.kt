package com.smach.zapmancer.users.routing

import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.respondResult
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.users.service.UsersService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Profile routes:
 *   GET  /users/profile           – own profile (authenticated)
 *   GET  /users/profile/{userId}  – any user's public profile
 *   PUT  /users/profile           – update own profile (authenticated)
 *   POST /users/{userId}/hire     – dispatch hire notification (authenticated)
 */
fun Route.usersRouting() {
    val service by inject<UsersService>()

    route("/users") {
        authenticate("local-jwt") {
            /** GET /users/profile — own profile */
            get("/profile") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                call.respondResult(service.getOwnProfile(principal.uid))
            }

            /** PUT /users/profile — update own profile */

            put("/profile") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val request = call.receive<UpdateProfileRequest>()
                call.respondResult(service.updateProfile(principal.uid, request))
            }

            /** GET /users/profile/{userId} — public profile */
            get("/profile/{userId}") {
                val userId =
                    call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                call.respondResult(service.getPublicProfile(userId))
            }

            /** POST /users/{userId}/hire */
            post("/{userId}/hire") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val freelancerId = call.parameters["userId"]
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                call.respondResult(service.hireFreelancer(principal.uid, freelancerId))
            }
        }
    }
}
