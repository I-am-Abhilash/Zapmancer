package com.smach.zapmancer.auth.routing

import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.ForgotPasswordRequest
import com.smach.zapmancer.core.common.dto.LoginRequest
import com.smach.zapmancer.core.common.dto.RefreshTokenRequest
import com.smach.zapmancer.core.common.dto.SignUpRequest
import com.smach.zapmancer.core.common.dto.VerifyOtpRequest
import com.smach.zapmancer.core.common.dto.AuthResponse
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
 *   POST /auth/web/login
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
            /**
             * Authenticate a user with email and password (Mobile/API).
             *
             * Request: [LoginRequest] Account credentials
             *
             * Responses:
             *   – 200 [ApiResponse<AuthResponse>] Authentication successful.
             *   – 401 [ApiResponse<Unit>] Invalid credentials.
             *
             * Tags: Authentication
             */
            post("/login") {
                val req = call.receive<LoginRequest>()
                val result = service.login(
                    email = req.email,
                    password = req.password,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Authenticate a web application user and attach HttpOnly refresh cookie.
             *
             * Request: [LoginRequest] Web account credentials
             *
             * Responses:
             *   – 200 [ApiResponse<WebLoginResponse>] Web login successful.
             *   – 401 [ApiResponse<Unit>] Invalid credentials.
             *
             * Tags: Authentication
             */
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

            /**
             * Register a new user account.
             *
             * Request: [SignUpRequest] User sign-up payload
             *
             * Responses:
             *   – 200 [ApiResponse<AuthResponse>] Registration successful.
             *   – 409 [ApiResponse<Unit>] Username or email already in use.
             *
             * Tags: Authentication
             */
            post("/register") {
                val req = call.receive<SignUpRequest>()
                val result = service.register(
                    req.username,
                    req.email,
                    req.password,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Request password reset OTP.
             *
             * Request: [ForgotPasswordRequest] Account email address
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] OTP code dispatched.
             *
             * Tags: Authentication
             */
            post("/forgot-password") {
                val req = call.receive<ForgotPasswordRequest>()
                val result = service.forgotPassword(req.email)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Verify password reset OTP code.
             *
             * Request: [VerifyOtpRequest] Email and 6-digit OTP code
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] OTP verified.
             *   – 400 [ApiResponse<Unit>] Invalid or expired OTP code.
             *
             * Tags: Authentication
             */
            post("/verify-otp") {
                val req = call.receive<VerifyOtpRequest>()
                val result = service.verifyOtp(
                    req.email,
                    req.code,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Refresh JWT access token.
             *
             * Request: [RefreshTokenRequest] Refresh token payload
             *
             * Responses:
             *   – 200 [ApiResponse<AuthResponse>] Tokens refreshed successfully.
             *   – 401 [ApiResponse<Unit>] Invalid or expired refresh token.
             *
             * Tags: Authentication
             */
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
            /**
             * Logout user session.
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Logged out successfully.
             *   – 401 [ApiResponse<Unit>] Missing or invalid token.
             *
             * Tags: Authentication
             */
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


