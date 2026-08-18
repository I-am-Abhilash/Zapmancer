package com.smach.zapmancer.auth.routing

import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.AuthResponse
import com.smach.zapmancer.core.common.dto.ChangePasswordRequest
import com.smach.zapmancer.core.common.dto.ForgotPasswordRequest
import com.smach.zapmancer.core.common.dto.LoginRequest
import com.smach.zapmancer.core.common.dto.RefreshTokenRequest
import com.smach.zapmancer.core.common.dto.ResetPasswordRequest
import com.smach.zapmancer.core.common.dto.SendEmailVerificationRequest
import com.smach.zapmancer.core.common.dto.SendPhoneOtpRequest
import com.smach.zapmancer.core.common.dto.SignUpRequest
import com.smach.zapmancer.core.common.dto.VerificationStatusResponse
import com.smach.zapmancer.core.common.dto.VerifyEmailRequest
import com.smach.zapmancer.core.common.dto.VerifyOtpRequest
import com.smach.zapmancer.core.common.dto.VerifyPhoneOtpRequest
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import io.ktor.http.Cookie
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.Serializable
import org.koin.ktor.ext.inject

/**
 * Authentication and Verification routes for Zapmancer:
 *   POST /auth/login
 *   POST /auth/web/login
 *   POST /auth/register
 *   POST /auth/forgot-password
 *   POST /auth/verify-otp
 *   POST /auth/reset-password
 *   POST /auth/refresh
 *   POST /auth/logout (authenticated)
 *   GET  /auth/verification-status (authenticated)
 *   POST /auth/phone/send-otp (authenticated)
 *   POST /auth/phone/verify (authenticated)
 *   POST /auth/email/send-verification (authenticated)
 *   POST /auth/email/verify (authenticated)
 */
fun Route.authRouting() {
    val service by inject<AuthService>()

    rateLimit(RateLimitName("auth")) {
        route("/auth") {
            /**
             * Authenticate a user with email and password (Mobile/API).
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
             */
            post("/register") {
                val req = call.receive<SignUpRequest>()
                val result = service.register(
                    username = req.username,
                    email = req.email,
                    password = req.password,
                )
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = result))
            }

            rateLimit(RateLimitName("otp")) {
                /**
                 * Request password reset OTP via Resend.
                 */
                post("/forgot-password") {
                    val req = call.receive<ForgotPasswordRequest>()
                    val result = service.forgotPassword(req.email)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Verify password reset OTP code.
                 */
                post("/verify-otp") {
                    val req = call.receive<VerifyOtpRequest>()
                    val result = service.verifyOtp(
                        email = req.email,
                        code = req.code,
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Set new password using verified OTP code.
                 */
                post("/reset-password") {
                    val req = call.receive<ResetPasswordRequest>()
                    val result = service.resetPassword(
                        email = req.email,
                        code = req.code,
                        newPassword = req.newPassword
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }
            }

            /**
             * Refresh JWT access token.
             */
            post("/refresh") {
                val req = call.receive<RefreshTokenRequest>()
                val result = service.refresh(req.refreshToken)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }

    authenticate("local-jwt") {
        route("/auth") {
            /**
             * Logout user session.
             */
            post("/logout") {
                call.respond(
                    ApiResponse(
                        success = true,
                        data = CommonResponse(success = true, message = "Logged out."),
                    ),
                )
            }

            /**
             * Change password for authenticated user.
             */
            post("/change-password") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<ChangePasswordRequest>()
                val result = service.changePassword(principal.uid, req.currentPassword, req.newPassword)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Get user email, phone, and identity verification status.
             */
            get("/verification-status") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val status = service.getVerificationStatus(principal.uid)
                call.respond(ApiResponse(success = true, data = status))
            }

            rateLimit(RateLimitName("otp")) {
                /**
                 * Dispatch an SMS verification OTP to a user's phone via Telnyx.
                 */
                post("/phone/send-otp") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val req = call.receive<SendPhoneOtpRequest>()
                    val result = service.sendPhoneOtp(principal.uid, req.phoneNumber)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Verify phone SMS OTP code.
                 */
                post("/phone/verify") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val req = call.receive<VerifyPhoneOtpRequest>()
                    val result = service.verifyPhoneOtp(principal.uid, req.code)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Dispatch an email verification OTP via Resend.
                 */
                post("/email/send-verification") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val result = service.sendEmailVerification(principal.uid)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Verify email OTP code.
                 */
                post("/email/verify") {
                    val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                    val req = call.receive<VerifyEmailRequest>()
                    val result = service.verifyEmail(principal.uid, req.code)
                    call.respond(ApiResponse(success = true, data = result))
                }
            }
        }
    }
}

@Serializable
data class WebLoginResponse(
    val accessToken: String,
)
