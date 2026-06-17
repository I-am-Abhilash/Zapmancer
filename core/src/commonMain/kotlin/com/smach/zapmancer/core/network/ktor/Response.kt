package com.smach.zapmancer.core.network.ktor


import kotlinx.serialization.Serializable

/**
 * A standardized wrapper for all API responses.
 * This ensures that the frontend always receives a consistent JSON structure.
 * @param success Indicates if the request was successful.
 * @param data The actual payload, present only when success is true; otherwise null.
 * @param error Details about what went wrong, present only when success is false; otherwise null.
 */
@Serializable
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiError? = null
)

/**
 * Details of an API error.
 * @param code A machine-readable string (e.g., "NOT_FOUND").
 * @param message A human-readable description of the error.
 */
@Serializable
data class ApiError(
    val code: String,
    val message: String
)