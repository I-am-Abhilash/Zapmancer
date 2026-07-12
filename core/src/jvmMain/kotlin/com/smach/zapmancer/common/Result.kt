package com.smach.zapmancer.common

import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

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

typealias ApiResponse<T> = com.smach.zapmancer.core.network.ktor.ApiResponse<T>
typealias ApiError = com.smach.zapmancer.core.network.ktor.ApiError
typealias CommonResponse = com.smach.zapmancer.core.common.dto.CommonResponse
