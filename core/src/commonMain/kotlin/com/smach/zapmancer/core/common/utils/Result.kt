package com.smach.zapmancer.core.common.utils

/**
 * Categorized error types for the entire application.
 */
sealed interface DataError {
    enum class Network : DataError {
        SERVICE_UNAVAILABLE,
        CLIENT_ERROR,
        NO_INTERNET,
        SERIALIZATION,
        UNAUTHORIZED,
        UNKNOWN,
    }

    enum class Local : DataError {
        DISK_FULL,
        PERMISSION_DENIED,
        INVALID_INPUT,
        UNKNOWN,
    }
}

/**
 * A discriminated union for success/failure with categorized errors.
 */
sealed interface Result<out D, out E : DataError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : DataError>(val error: E, val throwable: Throwable? = null) : Result<Nothing, E>
}
