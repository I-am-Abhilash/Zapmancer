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
 * Profile and User management routing module.
 */
fun Route.usersRouting() {
    val service by inject<UsersService>()

    route("/users") {
        authenticate("local-jwt") {
            /**
             * Fetch authenticated user profile
             *
             * Retrieves the complete profile details of the currently authenticated user, including portfolio items and reviews.
             *
             * @tags Users & Profiles
             * @security BearerAuth
             * @response 200 Successfully retrieved user profile. [UserProfile]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Update user profile
             *
             * Updates the authenticated user profile information such as display name, role title, bio, skills, and avatar URL.
             *
             * @tags Users & Profiles
             * @security BearerAuth
             * @response 200 Profile updated successfully. [UserProfile]
             * @response 400 Invalid profile payload. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Deactivate user account
             *
             * Deactivates and soft-deletes the authenticated user account and revokes active sessions.
             *
             * @tags Users & Profiles
             * @security BearerAuth
             * @response 200 Account deactivated successfully. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Fetch public user profile
             *
             * Retrieves the public profile data of any registered user by their unique user identifier.
             *
             * @tags Users & Profiles
             * @path userId The unique user identifier.
             * @response 200 Successfully retrieved public profile. [UserProfile]
             * @response 400 Missing or invalid user id. [ApiError]
             * @response 404 User account not found. [ApiError]
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
             * Send hire proposal notification
             *
             * Dispatches a direct hire proposal notification to a target freelancer.
             *
             * @tags Users & Profiles
             * @security BearerAuth
             * @path userId The unique freelancer user identifier.
             * @response 200 Hire proposal dispatched successfully. [CommonResponse]
             * @response 400 Missing target freelancer id. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Submit user review and rating
             *
             * Submits a star rating and written testimonial review for a target user profile.
             *
             * @tags Users & Profiles
             * @security BearerAuth
             * @path userId The unique target user identifier.
             * @response 201 Review created successfully. [Review]
             * @response 400 Invalid rating value or missing fields. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * List user reviews and ratings
             *
             * Retrieves a paginated list of reviews and star ratings received by the specified user.
             *
             * @tags Users & Profiles
             * @path userId The unique target user identifier.
             * @query page The page index to fetch (1-indexed).
             * @query limit Maximum number of reviews per page.
             * @response 200 List of reviews. [List<Review>]
             * @response 400 Missing target user id. [ApiError]
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
