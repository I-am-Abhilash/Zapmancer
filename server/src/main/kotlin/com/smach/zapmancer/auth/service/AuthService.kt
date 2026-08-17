package com.smach.zapmancer.auth.service

import at.favre.lib.crypto.bcrypt.BCrypt
import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.AuthResponse
import com.smach.zapmancer.core.common.dto.VerificationStatusResponse
import com.smach.zapmancer.core.security.JwtConfig
import com.smach.zapmancer.core.verification.ResendEmailService
import com.smach.zapmancer.core.verification.TelnyxSmsService
import org.koin.core.annotation.Single
import java.util.UUID
import kotlin.random.Random

/**
 * AuthService handles all authentication and verification business logic.
 * Integrated with Resend (Email) and Telnyx (SMS Phone).
 */
@Single
class AuthService(
    private val repository: AuthRepository,
    private val resendEmailService: ResendEmailService? = null,
    private val telnyxSmsService: TelnyxSmsService? = null
) {

    // ------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------

    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): AuthResponse {
        if (repository.findByEmail(email) != null) {
            throw ApiException(
                ErrorCode.CONFLICT,
                "An account with this email already exists.",
            )
        }
        if (repository.usernameExists(username)) {
            throw ApiException(ErrorCode.CONFLICT, "Username is already taken.")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val id = "user_${UUID.randomUUID().toString().replace("-", "").take(12)}"

        repository.createUser(id, username, email, passwordHash)

        // Automatically dispatch welcome email verification code via Resend
        val code = (100000..999999).random().toString()
        repository.saveOtp(email, code)
        resendEmailService?.sendVerificationOtp(email, code, username)

        val tokens = JwtConfig.generateTokens(id, email)
        return AuthResponse(
            id = id,
            email = email,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            isNewUser = true,
        )
    }

    // ------------------------------------------------------------------
    // Login
    // ------------------------------------------------------------------

    suspend fun login(email: String, password: String): AuthResponse {
        val user = repository.findByEmail(email, includeDeleted = true)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials.")

        val hash = user.passwordHash
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials.")

        val verified = BCrypt.verifyer().verify(password.toCharArray(), hash).verified
        if (!verified) {
            throw ApiException(ErrorCode.UNAUTHORIZED, "Invalid credentials.")
        }

        // Reactivate soft-deleted accounts on login
        if (user.isDeleted) {
            repository.reactivateAccount(user.id)
        }

        val tokens = JwtConfig.generateTokens(user.id, user.email)
        return AuthResponse(
            id = user.id,
            email = user.email,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            isNewUser = false,
        )
    }

    // ------------------------------------------------------------------
    // Token Refresh
    // ------------------------------------------------------------------

    suspend fun refresh(refreshToken: String): AuthResponse {
        val userId = JwtConfig.verifyRefreshToken(refreshToken)
            ?: throw ApiException(
                ErrorCode.UNAUTHORIZED,
                "Invalid or expired refresh token.",
            )

        val user = repository.findById(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")

        val tokens = JwtConfig.generateTokens(user.id, user.email)
        return AuthResponse(
            id = user.id,
            email = user.email,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )
    }

    // ------------------------------------------------------------------
    // Forgot Password — OTP Flow (Resend)
    // ------------------------------------------------------------------

    suspend fun forgotPassword(email: String): CommonResponse {
        val user = repository.findByEmail(email)
        if (user != null) {
            val code = (100000..999999).random().toString()
            repository.saveOtp(email, code)
            resendEmailService?.sendPasswordResetOtp(email, code)
        }
        return CommonResponse(success = true, message = "OTP verification code sent to your email.")
    }

    // ------------------------------------------------------------------
    // Email Verification (Resend)
    // ------------------------------------------------------------------

    suspend fun sendEmailVerification(userId: String): CommonResponse {
        val user = repository.findById(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")

        val code = (100000..999999).random().toString()
        repository.saveOtp(user.email, code)
        resendEmailService?.sendVerificationOtp(user.email, code, user.username)

        return CommonResponse(success = true, message = "Verification code dispatched to ${user.email}.")
    }

    suspend fun verifyEmail(userId: String, code: String): CommonResponse {
        val user = repository.findById(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")

        val valid = repository.verifyAndConsumeOtp(user.email, code)
        if (valid) {
            repository.setUserEmailVerified(userId)
            return CommonResponse(success = true, message = "Email verified successfully.")
        } else {
            throw ApiException(ErrorCode.BAD_REQUEST, "Invalid or expired verification code.")
        }
    }

    // ------------------------------------------------------------------
    // Phone Verification (Telnyx)
    // ------------------------------------------------------------------

    suspend fun sendPhoneOtp(userId: String, phoneNumber: String): CommonResponse {
        if (phoneNumber.length < 8 || !phoneNumber.replace("+", "").all { it.isDigit() }) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Invalid phone number format. Please provide E.164 format (e.g., +1234567890).")
        }

        val code = (100000..999999).random().toString()
        repository.savePhoneOtp(userId, phoneNumber, code)
        telnyxSmsService?.sendOtpSms(phoneNumber, code)

        return CommonResponse(success = true, message = "SMS verification code dispatched to $phoneNumber.")
    }

    suspend fun verifyPhoneOtp(userId: String, code: String): CommonResponse {
        val verifiedPhone = repository.verifyAndConsumePhoneOtp(userId, code)
            ?: throw ApiException(ErrorCode.BAD_REQUEST, "Invalid or expired phone verification code.")

        repository.setUserPhoneVerified(userId, verifiedPhone)
        return CommonResponse(success = true, message = "Phone number ($verifiedPhone) verified successfully.")
    }

    // ------------------------------------------------------------------
    // Verification Status
    // ------------------------------------------------------------------

    suspend fun getVerificationStatus(userId: String): VerificationStatusResponse {
        return repository.getVerificationStatus(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")
    }
}
