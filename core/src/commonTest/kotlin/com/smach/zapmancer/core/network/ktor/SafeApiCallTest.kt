package com.smach.zapmancer.core.network.ktor

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.io.IOException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests [safeApiCall]:
 *  - 2xx with success=true envelope → Result.Success(data)
 *  - 2xx with success=false envelope → Result.Error(CLIENT_ERROR)
 *  - 401 → UNAUTHORIZED
 *  - 4xx → CLIENT_ERROR
 *  - 5xx → SERVICE_UNAVAILABLE
 *  - IOException → NO_INTERNET
 *  - SerializationException → SERIALIZATION
 */
class SafeApiCallTest {

    @Serializable
    private data class Sample(val message: String)

    @Test
    fun `2xx with success envelope returns Success`() = runTest {
        val client = mockClient(
            """
            { "success": true, "data": { "message": "hello" } }
            """.trimIndent(),
        )

        val result = safeApiCall<Sample> { client.get("/ping") }

        assertTrue(result is Result.Success, "Expected Success but got $result")
        assertEquals("hello", result.data.message)
    }

    @Test
    fun `2xx with success=false envelope returns CLIENT_ERROR`() = runTest {
        val client = mockClient(
            """
            { "success": false, "error": { "code": "OOPS", "message": "nope" } }
            """.trimIndent(),
        )

        val result = safeApiCall<Sample> { client.get("/ping") }

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.CLIENT_ERROR, result.error)
    }

    @Test
    fun `401 response maps to UNAUTHORIZED`() = runTest {
        val client = mockClientError(HttpStatusCode.Unauthorized)
        val result = safeApiCall<Sample> { client.get("/ping") }
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.UNAUTHORIZED, result.error)
    }

    @Test
    fun `4xx response maps to CLIENT_ERROR`() = runTest {
        val client = mockClientError(HttpStatusCode.NotFound)
        val result = safeApiCall<Sample> { client.get("/ping") }
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.CLIENT_ERROR, result.error)
    }

    @Test
    fun `5xx response maps to SERVICE_UNAVAILABLE`() = runTest {
        val client = mockClientError(HttpStatusCode.InternalServerError)
        val result = safeApiCall<Sample> { client.get("/ping") }
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.SERVICE_UNAVAILABLE, result.error)
    }

    @Test
    fun `IOException maps to NO_INTERNET`() = runTest {
        val client = HttpClient(
            MockEngine { _ ->
                throw IOException("connection refused")
            },
        ) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }
        }

        val result = safeApiCall<Sample> { client.get("/ping") }
        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.NO_INTERNET, result.error)
    }

    @Test
    fun `malformed body maps to SERIALIZATION`() = runTest {
        val client = mockClient("{not valid json")
        val result = safeApiCall<Sample> { client.get("/ping") }
        assertTrue(result is Result.Error)
        assertEquals(
            DataError.Network.SERIALIZATION,
            result.error,
            "Expected SERIALIZATION but got ${(result as Result.Error).error}",
        )
    }

    // ---- helpers ----

    private fun mockClient(body: String): HttpClient = HttpClient(
        MockEngine { _ ->
            respond(
                content = body,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
            )
        },
    ) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private fun mockClientError(status: HttpStatusCode): HttpClient = HttpClient(
        MockEngine { _ ->
            respondError(status)
        },
    )
}
