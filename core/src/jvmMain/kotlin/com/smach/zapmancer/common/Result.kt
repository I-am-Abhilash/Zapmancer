package com.smach.zapmancer.common

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond
import kotlinx.serialization.Serializable

/**
 * A standardized wrapper for all API responses.
 * This ensures that the frontend always receives a consistent JSON structure.
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null,
)

/**
 * Details of an API error.
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String,
)

/**
 * DomainResult is a 'Result' pattern used by Services and Repositories.
 */
sealed class DomainResult<out T> {
    data class Success<out T>(val data: T) : DomainResult<T>()
    data class Error(val code: ErrorCode, val message: String? = null) : DomainResult<Nothing>()
}

/**
 * Enumeration of possible error types mapped to their HTTP status codes.
 */
enum class ErrorCode(val httpStatusCode: HttpStatusCode) {
    BAD_REQUEST(HttpStatusCode.BadRequest),
    UNAUTHORIZED(HttpStatusCode.Unauthorized),
    FORBIDDEN(HttpStatusCode.Forbidden),
    NOT_FOUND(HttpStatusCode.NotFound),
    CONFLICT(HttpStatusCode.Conflict),
    INTERNAL_SERVER_ERROR(HttpStatusCode.InternalServerError),
}

/**
 * Shared response DTO for simple success/failure acknowledgements.
 * Used across home, projects, proposals, messages, notifications, settings.
 */
@Serializable
data class CommonResponse(
    val success: Boolean,
    val message: String,
)

/**
 * Extension function to automatically map a DomainResult to an HTTP response.
 */
suspend inline fun <reified T : Any> ApplicationCall.respondResult(result: DomainResult<T>) {
    when (result) {
        is DomainResult.Success -> {
            this.respond(
                status = HttpStatusCode.OK,
                message = ApiResponse(success = true, data = result.data),
            )
        }

        is DomainResult.Error -> {
            this.respond(
                status = result.code.httpStatusCode,
                message = ApiResponse<T>(
                    success = false,
                    error = ApiError(
                        code = result.code.name,
                        message = result.message ?: "An unexpected error occurred",
                    ),
                ),
            )
        }
    }
}
