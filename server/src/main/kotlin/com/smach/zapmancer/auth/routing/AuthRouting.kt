package com.smach.zapmancer.auth.routing

import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.ForgotPasswordRequest
import com.smach.zapmancer.core.common.dto.LoginRequest
import com.smach.zapmancer.core.common.dto.RefreshTokenRequest
import com.smach.zapmancer.core.common.dto.SignUpRequest
import com.smach.zapmancer.core.common.dto.VerifyOtpRequest
import com.smach.zapmancer.core.network.ktor.ApiResponse
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

/**
 * Auth routes matching the Zapmancer API spec:
 *   POST /auth/login
 *   POST /auth/register
 *   POST /auth/forgot-password
 *   POST /auth/verify-otp
 *   POST /auth/refresh
 *   POST /auth/logout  (authenticated)
 */
fun Route.authRouting() {
    val service by inject<AuthService>()

    rateLimit(RateLimitName("auth")) {
        route("/auth") {
            // Android / iOS
            post("/login") {
                val req = call.receive<LoginRequest>()
                val result = service.login(
                    email = req.email,
                    password = req.password,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            // Web
            post("/web/login") {
                val req = call.receive<LoginRequest>()
                val tokens = service.login(
                    email = req.email,
                    password = req.password,
                )

                // Refresh token is stored in browser and cannot be accessed by JavaScript
                call.response.cookies.append(
                    Cookie(
                        name = "refresh_token",
                        value = tokens.refreshToken,
                        path = "/auth",
                        secure = true,
                        httpOnly = true,
                        extensions = mapOf(
                            "SameSite" to "Lax",
                        ),
                    ),
                )

                call.respond(
                    status = HttpStatusCode.OK,
                    message = ApiResponse(
                        success = true,
                        data = WebLoginResponse(
                            accessToken = tokens.accessToken,
                        ),
                    ),
                )
            }

            post("/register") {
                val req = call.receive<SignUpRequest>()
                val result = service.register(
                    req.username,
                    req.email,
                    req.password,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            post("/forgot-password") {
                val req = call.receive<ForgotPasswordRequest>()
                val result = service.forgotPassword(req.email)
                call.respond(ApiResponse(success = true, data = result))
            }

            post("/verify-otp") {
                val req = call.receive<VerifyOtpRequest>()
                val result = service.verifyOtp(
                    req.email,
                    req.code,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            // Mobile refresh
            post("/refresh") {
                val req = call.receive<RefreshTokenRequest>()
                val result = service.refresh(req.refreshToken)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }

    // Logout is authenticated (client must present a valid JWT) and intentionally
    // lives outside the public rate-limited block. JWTs are stateless on this
    // server, so the handler just acknowledges — token revocation is the client's
    // responsibility (drop it from local storage).
    authenticate("local-jwt") {
        route("/auth") {
            post("/logout") {
                call.respond(
                    ApiResponse(
                        success = true,
                        data = CommonResponse(success = true, message = "Logged out."),
                    ),
                )
            }
        }
    }
}

@Serializable
data class WebLoginResponse(
    val accessToken: String,
)

