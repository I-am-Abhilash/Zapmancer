package com.smach.zapmancer.core.network.ktor

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import io.github.aakira.napier.Napier
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> safeApiCall(
    block: () -> HttpResponse,
): Result<T, DataError.Network> = try {
    val response = block()

    if (response.status.isSuccess()) {
        val apiResponse = response.body<ApiResponse<T>>()

        if (apiResponse.success && apiResponse.data != null) {
            Result.Success(apiResponse.data)
        } else {
            Napier.e(
                "API returned success=false. " +
                        "Code=${apiResponse.error?.code}, " +
                        "Message=${apiResponse.error?.message}",
            )

            Result.Error(DataError.Network.CLIENT_ERROR)
        }
    } else {
        val errorType = when (response.status.value) {
            401 -> DataError.Network.UNAUTHORIZED
            in 400..499 -> DataError.Network.CLIENT_ERROR
            in 500..599 -> DataError.Network.SERVICE_UNAVAILABLE
            else -> DataError.Network.UNKNOWN
        }

        Napier.e("API Error: ${response.status.value}")
        Result.Error(errorType)
    }
} catch (e: Exception) {
    val errorType = when {
        e is IOException -> DataError.Network.NO_INTERNET

        e is SerializationException ||
                e is NoTransformationFoundException ||
                // JS/Firefox (and some Ktor wrapping paths) bury the original
                // SerializationException inside the cause chain. Walk it so we still
                // surface SERIALIZATION to the caller instead of falling through to UNKNOWN.
                e.hasCauseMatching { it is SerializationException } ||
                e.hasCauseMatching { it is NoTransformationFoundException } ->
            DataError.Network.SERIALIZATION

        else -> DataError.Network.UNKNOWN
    }

    Napier.e("Network Exception: ${e.message}", e)
    Result.Error(errorType, e)
}

@PublishedApi
internal fun Throwable.hasCauseMatching(predicate: (Throwable) -> Boolean): Boolean {
    var current: Throwable? = cause
    while (current != null) {
        if (predicate(current)) return true
        current = current.cause
    }
    return false
}
