package com.smach.zapmancer.core.common

import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import io.ktor.server.response.respond

/**
 * Custom domain exception thrown by service and repository layers.
 * Handled centrally by Ktor StatusPages plugin.
 */
class ApiException(
    val code: ErrorCode,
    override val message: String,
) : Exception(message)

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

typealias ApiResponse<T> = ApiResponse<T>
typealias ApiError = ApiError
typealias CommonResponse = CommonResponse

