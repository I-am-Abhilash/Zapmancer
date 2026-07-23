@file:Suppress("ConstructorParameterNaming", "TooGenericExceptionCaught", "SwallowedException")

package com.smach.zapmancer.core.recommendations

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Serializable
data class GorseUser(val UserId: String, val Labels: List<String> = emptyList())

@Serializable
data class GorseItem(
    val ItemId: String,
    val Categories: List<String> = emptyList(),
    val Timestamp: String = "",
)

@Serializable
data class GorseFeedback(
    val FeedbackType: String,
    val UserId: String,
    val ItemId: String,
    val Timestamp: String = "",
)

/**
 * Client for the Gorse Recommendation Engine.
 * It sends user/item/feedback data and fetches recommendations.
 */
class GorseClient(private val baseUrl: String = "http://localhost:8088") {
    private val logger = LoggerFactory.getLogger(GorseClient::class.java)
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun insertUser(uid: String) {
        try {
            val response: HttpResponse = client.post("$baseUrl/api/user") {
                contentType(ContentType.Application.Json)
                setBody(GorseUser(uid))
            }
            if (!response.status.isSuccess()) {
                logger.error("Gorse Error (insertUser): ${response.status} ${response.body<String>()}")
            }
        } catch (e: Exception) {
            logger.error("Gorse Exception (insertUser): ${e.message}")
        }
    }

    suspend fun insertItem(itemId: String, categories: List<String>, timestamp: String) {
        try {
            val response: HttpResponse = client.post("$baseUrl/api/item") {
                contentType(ContentType.Application.Json)
                setBody(GorseItem(itemId, categories, timestamp))
            }
            if (!response.status.isSuccess()) {
                logger.error("Gorse Error (insertItem): ${response.status} ${response.body<String>()}")
            }
        } catch (e: Exception) {
            logger.error("Gorse Exception (insertItem): ${e.message}")
        }
    }

    suspend fun sendFeedback(type: String, userId: String, itemId: String) {
        try {
            val response: HttpResponse = client.post("$baseUrl/api/feedback") {
                contentType(ContentType.Application.Json)
                setBody(listOf(GorseFeedback(type, userId, itemId)))
            }
            if (!response.status.isSuccess()) {
                logger.error("Gorse Error (sendFeedback): ${response.status} ${response.body<String>()}")
            }
        } catch (e: Exception) {
            logger.error("Gorse Exception (sendFeedback): ${e.message}")
        }
    }

    suspend fun getRecommended(userId: String, n: Int = 10): List<String> = try {
        client.get("$baseUrl/api/recommend/$userId?n=$n").body()
    } catch (e: Exception) {
        emptyList()
    }

    /** Convenience overload — registers a project with no initial categories/timestamp. */
    suspend fun insertItem(itemId: String) = insertItem(itemId, emptyList(), "")

    /** Alias for sendFeedback to match Zapmancer naming. */
    suspend fun insertFeedback(type: String, userId: String, itemId: String) = sendFeedback(type, userId, itemId)
}
