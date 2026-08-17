package com.smach.zapmancer.core.verification

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory

@Serializable
data class TelnyxSendSmsRequest(
    val from: String,
    val to: String,
    val text: String
)

@Serializable
data class TelnyxSendSmsResponse(
    val data: TelnyxMessageData? = null
)

@Serializable
data class TelnyxMessageData(
    val id: String? = null,
    val record_type: String? = null
)

class TelnyxSmsService(
    private val apiKey: String? = null,
    private val fromNumber: String = "+18005550199"
) {
    private val logger = LoggerFactory.getLogger(TelnyxSmsService::class.java)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    /**
     * Send phone verification OTP code via Telnyx SMS.
     */
    suspend fun sendOtpSms(toPhoneNumber: String, code: String): Boolean {
        val normalizedNumber = if (toPhoneNumber.startsWith("+")) toPhoneNumber else "+$toPhoneNumber"
        val messageText = "Your Zapmancer verification code is: $code. Valid for 10 minutes. Do not share this code."

        if (apiKey.isNullOrBlank()) {
            logger.info("==================================================================")
            logger.info("[TELNYX MOCK] No API key configured. Logging SMS dispatch:")
            logger.info("To: $normalizedNumber")
            logger.info("Message: $messageText")
            logger.info("==================================================================")
            return true
        }

        return try {
            val response = client.post("https://api.telnyx.com/v2/messages") {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    TelnyxSendSmsRequest(
                        from = fromNumber,
                        to = normalizedNumber,
                        text = messageText
                    )
                )
            }

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created || response.status == HttpStatusCode.Accepted) {
                logger.info("Successfully dispatched SMS via Telnyx to $normalizedNumber")
                true
            } else {
                val errorBody = response.bodyAsText()
                logger.error("Telnyx SMS API error (${response.status}): $errorBody")
                false
            }
        } catch (e: Exception) {
            logger.error("Failed to dispatch SMS via Telnyx: ${e.message}", e)
            false
        }
    }
}
