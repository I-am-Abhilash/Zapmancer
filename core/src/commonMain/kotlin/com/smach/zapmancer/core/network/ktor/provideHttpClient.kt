package com.smach.zapmancer.core.network.ktor

import com.smach.zapmancer.core.network.model.RefreshTokenRequest
import com.smach.zapmancer.core.network.model.RefreshTokenResponse
import com.smach.zapmancer.core.network.session.SessionManager
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun provideHttpClient(sessionManager: SessionManager): HttpClient {
    return HttpClient {
        defaultRequest {
            url(NetworkConstants.BASE_URL)
            contentType(ContentType.Application.Json)
        }
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                },
            )
        }
        install(WebSockets)
        install(Logging) {
            level = LogLevel.ALL
            logger =
                object : Logger {
                    override fun log(message: String) {
                        Napier.v(tag = "HTTP Client", message = message)
                    }
                }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 30_000
        }
        install(HttpRequestRetry) {
            retryOnException(maxRetries = 3, retryOnTimeout = true)
            retryOnServerErrors(maxRetries = 2)
            exponentialDelay(base = 2.0, maxDelayMs = 8_000)
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val accessToken = sessionManager.getAccessToken()
                    val refreshToken = sessionManager.getRefreshToken()

                    if (
                        accessToken.isNullOrBlank() ||
                        refreshToken.isNullOrBlank()
                    ) {
                        null
                    } else {
                        BearerTokens(
                            accessToken = accessToken,
                            refreshToken = refreshToken,
                        )
                    }
                }
                refreshTokens {
                    val currentRefreshToken =
                        sessionManager.getRefreshToken()
                            ?: return@refreshTokens null
                    try {
                        val response = client.post("auth/refresh") {
                            markAsRefreshTokenRequest()
                            setBody(
                                RefreshTokenRequest(
                                    refreshToken = currentRefreshToken,
                                ),
                            )
                        }.body<ApiResponse<RefreshTokenResponse>>()

                        val user = response.data ?: return@refreshTokens null

                        val newAccessToken = user.accessToken
                        val newRefreshToken = user.refreshToken

                        sessionManager.saveSession(
                            userId = user.id,
                            accessToken = newAccessToken,
                            refreshToken = newRefreshToken,
                        )
                        BearerTokens(
                            accessToken = newAccessToken,
                            refreshToken = newRefreshToken,
                        )
                    } catch (e: Exception) {
                        Napier.e(
                            tag = "Auth",
                            message = "Token refresh failed",
                            throwable = e,
                        )
                        null
                    }
                }
            }
        }
    }
}
