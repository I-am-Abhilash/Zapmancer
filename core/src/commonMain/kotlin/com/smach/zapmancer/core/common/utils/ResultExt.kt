package com.smach.zapmancer.core.common.utils

/**
 * Helpers for working with the project's typed [Result] (a sealed interface
 * modelling `Result<D, E : DataError>`).
 *
 * These exist to keep repository implementations terse and consistent — call
 * sites read `.toUnit()` instead of writing `when (result) { is Success -> Success(Unit); is Error -> Error(...) }`.
 */

/** Identity for the typed Result; provided so repository call sites can chain uniformly. */
inline fun <D, E : DataError> Result<D, E>.orElsePropagate(): Result<D, E> = this

/**
 * Re-tag a successful payload of any non-Unit type as `Unit`. On [Result.Error] it is unchanged.
 * Useful for repositories whose endpoints don't return a meaningful body for write operations.
 */
fun <E : DataError> Result<*, E>.toUnitResult(): Result<Unit, E> = when (this) {
    is Result.Success -> Result.Success(Unit)
    is Result.Error -> this
}

/** Re-tag a Result whose success type is already Unit — no-op, but a clearer name. */
@Suppress("UNCHECKED_CAST")
fun <E : DataError> Result<Unit, E>.asUnit(): Result<Unit, E> = this

/**
 * Returns this result, or logs the error and returns [Result.Error] with the
 * underlying throwable (if any). Use in repositories where you want to attach
 * analytics to failures without altering the result type.
 */
inline fun <D, E : DataError> Result<D, E>.onErrorLog(
    onLog: (E, Throwable?) -> Unit,
): Result<D, E> {
    if (this is Result.Error) onLog(error, throwable)
    return this
}

/**
 * Kotlin-style fold over the typed [Result]. Runs [onSuccess] with the data or
 * [onError] with the typed error. Pure ergonomic sugar — no exceptions.
 */
inline fun <D, E : DataError, R> Result<D, E>.foldTyped(
    onSuccess: (D) -> R,
    onError: (E) -> R,
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(error)
}
