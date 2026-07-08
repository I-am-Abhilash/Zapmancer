package com.smach.zapmancer.auth.routing

import com.smach.zapmancer.auth.domain.AuthService
import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.respondResult
import com.smach.zapmancer.core.common.dto.ForgotPasswordRequest
import com.smach.zapmancer.core.common.dto.LoginRequest
import com.smach.zapmancer.core.common.dto.RefreshTokenRequest
import com.smach.zapmancer.core.common.dto.SignUpRequest
import com.smach.zapmancer.core.common.dto.VerifyOtpRequest
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
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
            post("/login") {
                val req = call.receive<LoginRequest>()
                call.respondResult(service.login(req.email, req.password))
            }

            post("/register") {
                val req = call.receive<SignUpRequest>()
                call.respondResult(service.register(req.username, req.email, req.password))
            }

            post("/forgot-password") {
                val req = call.receive<ForgotPasswordRequest>()
                call.respondResult(service.forgotPassword(req.email))
            }

            post("/verify-otp") {
                val req = call.receive<VerifyOtpRequest>()
                call.respondResult(service.verifyOtp(req.email, req.code))
            }

            post("/refresh") {
                val req = call.receive<RefreshTokenRequest>()
                call.respondResult(service.refresh(req.refreshToken))
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
                call.respondResult(
                    DomainResult.Success(CommonResponse(success = true, message = "Logged out.")),
                )
            }
        }
    }
}
