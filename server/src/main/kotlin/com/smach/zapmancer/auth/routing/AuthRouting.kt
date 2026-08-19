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
 * Authentication and Verification routes for Zapmancer.
 */
fun Route.authRouting() {
    val service by inject<AuthService>()

    rateLimit(RateLimitName("auth")) {
        route("/auth") {
            /**
             * Authenticate user credentials
             *
             * Authenticates a user with email and password for Mobile and API clients, returning JWT access and refresh tokens.
             *
             * @tags Authentication
             * @response 200 Successful authentication returning JWT tokens. [AuthResponse]
             * @response 400 Invalid credentials or malformed payload. [ApiError]
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
             * Authenticate web client with HttpOnly cookie
             *
             * Authenticates web browser clients and sets a secure HttpOnly refresh token cookie while returning the short-lived access token in the payload.
             *
             * @tags Authentication
             * @response 200 Successfully authenticated with HttpOnly cookie set. [WebLoginResponse]
             * @response 400 Invalid credentials or malformed payload. [ApiError]
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
             * Register a new user account
             *
             * Registers a new user account with email, username, and password, returning initial JWT authentication credentials.
             *
             * @tags Authentication
             * @response 201 Account registered successfully. [AuthResponse]
             * @response 400 Validation failure or account identifier conflict. [ApiError]
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
                 * Request password reset OTP
                 *
                 * Initiates the password recovery workflow by dispatching a 6-digit one-time verification code to the registered email address.
                 *
                 * @tags Authentication
                 * @response 200 Reset OTP dispatched successfully. [CommonResponse]
                 * @response 400 Invalid email address format. [ApiError]
                 */
                post("/forgot-password") {
                    val req = call.receive<ForgotPasswordRequest>()
                    val result = service.forgotPassword(req.email)
                    call.respond(ApiResponse(success = true, data = result))
                }

                /**
                 * Verify password reset OTP code
                 *
                 * Verifies whether the provided 6-digit OTP code matches the active challenge for the email address.
                 *
                 * @tags Authentication
                 * @response 200 OTP code verified successfully. [CommonResponse]
                 * @response 400 Invalid or expired OTP verification code. [ApiError]
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
                 * Reset account password with OTP
                 *
                 * Sets a new account password using the verified one-time code.
                 *
                 * @tags Authentication
                 * @response 200 Password reset successfully. [CommonResponse]
                 * @response 400 Invalid code or weak password. [ApiError]
                 */
                post("/reset-password") {
                    val req = call.receive<ResetPasswordRequest>()
                    val result = service.resetPassword(
                        email = req.email,
                        code = req.code,
                        newPassword = req.newPassword,
                    )
                    call.respond(ApiResponse(success = true, data = result))
                }
            }

            /**
             * Refresh JWT access token
             *
             * Exchanges an unexpired refresh token for a newly signed JWT access token.
             *
             * @tags Authentication
             * @response 200 Tokens refreshed successfully. [AuthResponse]
             * @response 401 Invalid or revoked refresh token. [ApiError]
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
             * Terminate authenticated session
             *
             * Invalidates the active user session and revokes authentication credentials.
             *
             * @tags Authentication
             * @security BearerAuth
             * @response 200 Session terminated successfully. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Change user password
             *
             * Updates the authenticated user password after verifying current credentials.
             *
             * @tags Authentication
             * @security BearerAuth
             * @response 200 Password updated successfully. [CommonResponse]
             * @response 400 Current password incorrect or new password validation failed. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
             * Fetch identity verification status
             *
             * Retrieves verification status flags for email, phone number, and government KYC identity.
             *
             * @tags Authentication, Identity Verification
             * @security BearerAuth
             * @response 200 Verification status flags. [VerificationStatusResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
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
                 * Dispatch SMS phone verification OTP
                 *
                 * Sends a 6-digit verification code via SMS to the provided phone number.
                 *
                 * @tags Authentication, Identity Verification
                 * @security BearerAuth
                 * @response 200 SMS OTP dispatched successfully. [CommonResponse]
                 * @response 400 Invalid phone number format. [ApiError]
                 * @response 401 Missing or invalid authentication token. [ApiError]
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
                 * Verify phone number OTP
                 *
                 * Confirms the SMS verification code and marks the user phone number as verified.
                 *
                 * @tags Authentication, Identity Verification
                 * @security BearerAuth
                 * @response 200 Phone number verified successfully. [CommonResponse]
                 * @response 400 Invalid or expired verification code. [ApiError]
                 * @response 401 Missing or invalid authentication token. [ApiError]
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
                 * Dispatch email verification code
                 *
                 * Sends an email verification OTP code to the authenticated user registered address.
                 *
                 * @tags Authentication, Identity Verification
                 * @security BearerAuth
                 * @response 200 Verification email sent successfully. [CommonResponse]
                 * @response 401 Missing or invalid authentication token. [ApiError]
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
                 * Verify email address OTP
                 *
                 * Confirms the email verification code and marks the user email as verified.
                 *
                 * @tags Authentication, Identity Verification
                 * @security BearerAuth
                 * @response 200 Email address verified successfully. [CommonResponse]
                 * @response 400 Invalid or expired verification code. [ApiError]
                 * @response 401 Missing or invalid authentication token. [ApiError]
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
