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
    val errorType = when (e) {
        is IOException -> DataError.Network.NO_INTERNET

        is SerializationException,
        is NoTransformationFoundException,
        -> DataError.Network.SERIALIZATION

        else -> DataError.Network.UNKNOWN
    }

    Napier.e("Network Exception: ${e.message}", e)
    Result.Error(errorType, e)
}
