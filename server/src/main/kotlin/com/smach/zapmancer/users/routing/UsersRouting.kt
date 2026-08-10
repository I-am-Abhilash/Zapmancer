package com.smach.zapmancer.users.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.core.network.ktor.ApiResponse
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
            /**
             * Retrieve authenticated user's own profile.
             *
             * Responses:
             *   – 200 [ApiResponse<UserProfile>] Authenticated user profile.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *   – 404 [ApiResponse<Unit>] Profile not found.
             *
             * Tags: Users
             */
            get("/profile") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val result = service.getOwnProfile(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Update authenticated user's profile.
             *
             * Request: [UpdateProfileRequest] Profile fields to update
             *
             * Responses:
             *   – 200 [ApiResponse<UserProfile>] Updated profile details.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Users
             */
            put("/profile") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val request = call.receive<UpdateProfileRequest>()
                val result = service.updateProfile(principal.uid, request)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Retrieve public user profile by ID.
             *
             * Path: userId [String] User ID
             *
             * Responses:
             *   – 200 [ApiResponse<UserProfile>] Public profile details.
             *   – 404 [ApiResponse<Unit>] Profile not found.
             *
             * Tags: Users
             */
            get("/profile/{userId}") {
                val userId =
                    call.parameters["userId"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val result = service.getPublicProfile(userId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Send a hire offer notification to a freelancer.
             *
             * Path: userId [String] Freelancer user ID
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Offer sent.
             *   – 404 [ApiResponse<Unit>] Freelancer not found.
             *
             * Tags: Users
             */
            post("/{userId}/hire") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val freelancerId = call.parameters["userId"]
                    ?: return@post call.respond(HttpStatusCode.BadRequest)
                val result = service.hireFreelancer(principal.uid, freelancerId)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}


