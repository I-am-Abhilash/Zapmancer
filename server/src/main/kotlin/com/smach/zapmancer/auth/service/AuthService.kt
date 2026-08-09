package com.smach.zapmancer.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.DomainResult
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.AuthResponse
import com.smach.zapmancer.core.security.JwtConfig
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton
import java.util.UUID

/**
 * AuthService handles all authentication business logic.
 * It depends only on its own [AuthRepository] — no cross-feature service dependency.
 */
@Single
class AuthService(private val repository: AuthRepository) {

    // ------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------

    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): DomainResult<AuthResponse> {
        if (repository.findByEmail(email) != null) {
            return DomainResult.Error(
                ErrorCode.CONFLICT,
                "An account with this email already exists.",
            )
        }
        if (repository.usernameExists(username)) {
            return DomainResult.Error(ErrorCode.CONFLICT, "Username is already taken.")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val id = "user_${UUID.randomUUID().toString().replace("-", "").take(12)}"

        repository.createUser(id, username, email, passwordHash)

        val tokens = JwtConfig.generateTokens(id, email)
        return DomainResult.Success(
            AuthResponse(
                id = id,
                email = email,
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                isNewUser = true,
            ),
        )
    }

    // ------------------------------------------------------------------
    // Login
    // ------------------------------------------------------------------

    suspend fun login(email: String, password: String): DomainResult<AuthResponse> {
        val user = repository.findByEmail(email, includeDeleted = true)
            ?: return DomainResult.Error(ErrorCode.UNAUTHORIZED, "Invalid credentials.")

        val hash = user.passwordHash
            ?: return DomainResult.Error(ErrorCode.UNAUTHORIZED, "Invalid credentials.")

        val verified = BCrypt.verifyer().verify(password.toCharArray(), hash).verified
        if (!verified) {
            return DomainResult.Error(ErrorCode.UNAUTHORIZED, "Invalid credentials.")
        }

        // Reactivate soft-deleted accounts on login
        if (user.isDeleted) {
            repository.reactivateAccount(user.id)
        }

        val tokens = JwtConfig.generateTokens(user.id, user.email)
        return DomainResult.Success(
            AuthResponse(
                id = user.id,
                email = user.email,
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
                isNewUser = false,
            ),
        )
    }

    // ------------------------------------------------------------------
    // Token Refresh
    // ------------------------------------------------------------------

    suspend fun refresh(refreshToken: String): DomainResult<AuthResponse> {
        val userId = JwtConfig.verifyRefreshToken(refreshToken)
            ?: return DomainResult.Error(
                ErrorCode.UNAUTHORIZED,
                "Invalid or expired refresh token.",
            )

        val user = repository.findById(userId)
            ?: return DomainResult.Error(ErrorCode.UNAUTHORIZED, "User not found.")

        val tokens = JwtConfig.generateTokens(user.id, user.email)
        return DomainResult.Success(
            AuthResponse(
                id = user.id,
                email = user.email,
                accessToken = tokens.accessToken,
                refreshToken = tokens.refreshToken,
            ),
        )
    }

    // ------------------------------------------------------------------
    // Forgot Password — OTP Flow
    // ------------------------------------------------------------------

    suspend fun forgotPassword(email: String): DomainResult<CommonResponse> {
        // We don't reveal whether the email exists (security best-practice)
        val user = repository.findByEmail(email)
        if (user != null) {
            val code = (100000..999999).random().toString()
            repository.saveOtp(email, code)
            // TODO: integrate email provider (SendGrid, SES, etc.) to send OTP
            println("[AUTH] OTP for $email: $code") // dev logging only
        }
        return DomainResult.Success(
            CommonResponse(success = true, message = "OTP verification code sent to your email."),
        )
    }

    // ------------------------------------------------------------------
    // Verify OTP
    // ------------------------------------------------------------------

    suspend fun verifyOtp(email: String, code: String): DomainResult<CommonResponse> {
        val valid = repository.verifyAndConsumeOtp(email, code)
        return if (valid) {
            DomainResult.Success(
                CommonResponse(
                    success = true,
                    message = "OTP verified successfully.",
                ),
            )
        } else {
            DomainResult.Error(ErrorCode.BAD_REQUEST, "Invalid or expired OTP code.")
        }
    }
}
