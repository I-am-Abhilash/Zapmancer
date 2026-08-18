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
data class ResendSendEmailRequest(
    val from: String,
    val to: List<String>,
    val subject: String,
    val html: String,
)

@Serializable
data class ResendSendEmailResponse(
    val id: String? = null,
    val message: String? = null,
    val name: String? = null,
)

class ResendEmailService(
    private val apiKey: String? = null,
    private val fromEmail: String = "Zapmancer <noreply@zapmancer.com>",
) {
    private val logger = LoggerFactory.getLogger(ResendEmailService::class.java)

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                },
            )
        }
    }

    /**
     * Send email verification OTP code.
     */
    suspend fun sendVerificationOtp(toEmail: String, code: String, username: String? = null): Boolean {
        val subject = "Verify your Zapmancer account - Code: $code"
        val html = buildVerificationEmailHtml(code, username ?: toEmail.substringBefore("@"))
        return sendEmail(toEmail, subject, html)
    }

    /**
     * Send password reset OTP code.
     */
    suspend fun sendPasswordResetOtp(toEmail: String, code: String): Boolean {
        val subject = "Reset your Zapmancer password - Code: $code"
        val html = buildPasswordResetHtml(code)
        return sendEmail(toEmail, subject, html)
    }

    private suspend fun sendEmail(toEmail: String, subject: String, html: String): Boolean {
        if (apiKey.isNullOrBlank()) {
            logger.info("==================================================================")
            logger.info("[RESEND MOCK] No API key configured. Logging email dispatch:")
            logger.info("To: $toEmail")
            logger.info("Subject: $subject")
            logger.info("HTML: $html")
            logger.info("==================================================================")
            return true
        }

        return try {
            val response = client.post("https://api.resend.com/emails") {
                header("Authorization", "Bearer $apiKey")
                contentType(ContentType.Application.Json)
                setBody(
                    ResendSendEmailRequest(
                        from = fromEmail,
                        to = listOf(toEmail),
                        subject = subject,
                        html = html,
                    ),
                )
            }

            if (response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Created) {
                logger.info("Successfully sent email via Resend to $toEmail")
                true
            } else {
                val errorBody = response.bodyAsText()
                logger.error("Resend API error (${response.status}): $errorBody")
                false
            }
        } catch (e: Exception) {
            logger.error("Failed to dispatch email via Resend: ${e.message}", e)
            false
        }
    }

    private fun buildVerificationEmailHtml(code: String, username: String): String = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <style>
                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #0d1117; color: #e6edf3; padding: 40px 20px; }
                .container { max-width: 540px; margin: 0 auto; background: #161b22; border-radius: 12px; border: 1px solid #30363d; padding: 32px; text-align: center; }
                .logo { font-size: 24px; font-weight: 800; color: #2ea043; letter-spacing: -0.5px; margin-bottom: 24px; }
                .title { font-size: 20px; font-weight: 700; color: #ffffff; margin-bottom: 12px; }
                .desc { font-size: 14px; color: #8b949e; line-height: 1.6; margin-bottom: 28px; }
                .otp-box { display: inline-block; background: #21262d; border: 1px dashed #2ea043; border-radius: 8px; padding: 16px 32px; font-size: 32px; font-weight: 800; letter-spacing: 8px; color: #3fb950; margin-bottom: 28px; }
                .footer { font-size: 12px; color: #484f58; margin-top: 24px; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="logo">⚡ ZAPMANCER</div>
                <div class="title">Verify Your Email Address</div>
                <div class="desc">Hello <strong>$username</strong>,<br>Use the following 6-digit verification code to confirm your email address on Zapmancer. This code expires in 10 minutes.</div>
                <div class="otp-box">$code</div>
                <div class="desc">If you did not request this verification, you can safely ignore this message.</div>
                <div class="footer">&copy; Zapmancer Inc. All rights reserved.</div>
            </div>
        </body>
        </html>
    """.trimIndent()

    private fun buildPasswordResetHtml(code: String): String = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <style>
                body { font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif; background-color: #0d1117; color: #e6edf3; padding: 40px 20px; }
                .container { max-width: 540px; margin: 0 auto; background: #161b22; border-radius: 12px; border: 1px solid #30363d; padding: 32px; text-align: center; }
                .logo { font-size: 24px; font-weight: 800; color: #f85149; letter-spacing: -0.5px; margin-bottom: 24px; }
                .title { font-size: 20px; font-weight: 700; color: #ffffff; margin-bottom: 12px; }
                .desc { font-size: 14px; color: #8b949e; line-height: 1.6; margin-bottom: 28px; }
                .otp-box { display: inline-block; background: #21262d; border: 1px dashed #f85149; border-radius: 8px; padding: 16px 32px; font-size: 32px; font-weight: 800; letter-spacing: 8px; color: #ff7b72; margin-bottom: 28px; }
                .footer { font-size: 12px; color: #484f58; margin-top: 24px; }
            </style>
        </head>
        <body>
            <div class="container">
                <div class="logo">⚡ ZAPMANCER</div>
                <div class="title">Password Reset Request</div>
                <div class="desc">We received a request to reset your Zapmancer password.<br>Enter the code below to reset your password. This code expires in 10 minutes.</div>
                <div class="otp-box">$code</div>
                <div class="desc">Never share this code with anyone. If you didn't request a reset, your account is still secure.</div>
                <div class="footer">&copy; Zapmancer Inc. All rights reserved.</div>
            </div>
        </body>
        </html>
    """.trimIndent()
}
