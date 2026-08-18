package com.smach.zapmancer.users.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateReviewRequest
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.users.service.UsersService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Profile and User routes:
 *   GET    /users/profile           – own profile (authenticated)
 *   PUT    /users/profile           – update own profile (authenticated)
 *   DELETE /users/account           – deactivate account (authenticated)
 *   GET    /users/profile/{userId}  – any user's public profile
 *   POST   /users/{userId}/hire     – dispatch hire notification (authenticated)
 *   POST   /users/{userId}/reviews  – submit rating review (authenticated)
 *   GET    /users/{userId}/reviews  – list paginated reviews (authenticated)
 */
fun Route.usersRouting() {
    val service by inject<UsersService>()

    route("/users") {
        authenticate("local-jwt") {
            /**
             * Retrieve authenticated user's own profile.
             */
            get("/profile") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.getOwnProfile(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Update authenticated user's profile.
             */
            put("/profile") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val request = call.receive<UpdateProfileRequest>()
                val result = service.updateProfile(principal.uid, request)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Deactivate/soft-delete authenticated user's own account.
             */
            delete("/account") {
                val principal = call.principal<UserPrincipal>() ?: return@delete call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.deactivateAccount(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Retrieve public user profile by ID.
             */
            get("/profile/{userId}") {
                val userId = call.parameters["userId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing user id")),
                )
                val result = service.getPublicProfile(userId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Send a hire offer notification to a freelancer.
             */
            post("/{userId}/hire") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val freelancerId = call.parameters["userId"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing freelancer user id")),
                )
                val result = service.hireFreelancer(principal.uid, freelancerId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Submit a review and rating for a user.
             */
            post("/{userId}/reviews") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val subjectId = call.parameters["userId"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing target user id")),
                )
                val request = call.receive<CreateReviewRequest>()
                val result = service.createReview(principal.uid, subjectId, request)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = result))
            }

            /**
             * List paginated reviews for a user.
             */
            get("/{userId}/reviews") {
                val subjectId = call.parameters["userId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing target user id")),
                )
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()
                    ?: call.request.queryParameters["pageSize"]?.toIntOrNull()
                    ?: 20
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val offset = ((page - 1) * limit).toLong()

                val result = service.getReviews(subjectId, limit, offset)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}
