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
import java.security.SecureRandom
import java.util.UUID

/**
 * AuthService handles all authentication and verification business logic.
 * Integrated with Resend (Email), Telnyx (SMS Phone), and SecureRandom.
 */
@Single
class AuthService(
    private val repository: AuthRepository,
    private val resendEmailService: ResendEmailService? = null,
    private val telnyxSmsService: TelnyxSmsService? = null
) {
    private val secureRandom = SecureRandom()

    private fun generateSecureOtp(): String {
        val codeNumber = 100_000 + secureRandom.nextInt(900_000)
        return codeNumber.toString()
    }

    // ------------------------------------------------------------------
    // Registration
    // ------------------------------------------------------------------

    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): AuthResponse {
        val normalizedEmail = email.trim().lowercase()
        val normalizedUsername = username.trim()

        if (password.length < 8) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Password must be at least 8 characters.")
        }
        if (repository.findByEmail(normalizedEmail) != null) {
            throw ApiException(
                ErrorCode.CONFLICT,
                "An account with this email already exists.",
            )
        }
        if (repository.usernameExists(normalizedUsername)) {
            throw ApiException(ErrorCode.CONFLICT, "Username is already taken.")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, password.toCharArray())
        val id = "user_${UUID.randomUUID().toString().replace("-", "").take(12)}"

        repository.createUser(id, normalizedUsername, normalizedEmail, passwordHash)

        // Automatically dispatch welcome email verification code via Resend
        val code = generateSecureOtp()
        repository.saveOtp(normalizedEmail, code)
        resendEmailService?.sendVerificationOtp(normalizedEmail, code, normalizedUsername)

        val tokens = JwtConfig.generateTokens(id, normalizedEmail, "FREELANCER")
        return AuthResponse(
            id = id,
            email = normalizedEmail,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
            isNewUser = true,
        )
    }

    // ------------------------------------------------------------------
    // Login
    // ------------------------------------------------------------------

    suspend fun login(email: String, password: String): AuthResponse {
        val normalizedEmail = email.trim().lowercase()
        val user = repository.findByEmail(normalizedEmail, includeDeleted = true)
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

        val tokens = JwtConfig.generateTokens(user.id, user.email, user.role)
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

        val tokens = JwtConfig.generateTokens(user.id, user.email, user.role)
        return AuthResponse(
            id = user.id,
            email = user.email,
            accessToken = tokens.accessToken,
            refreshToken = tokens.refreshToken,
        )
    }

    // ------------------------------------------------------------------
    // Authenticated Password Change
    // ------------------------------------------------------------------

    suspend fun changePassword(userId: String, currentPassword: String, newPassword: String): CommonResponse {
        if (newPassword.length < 8) {
            throw ApiException(ErrorCode.BAD_REQUEST, "New password must be at least 8 characters.")
        }

        val user = repository.findById(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")

        val hash = user.passwordHash
            ?: throw ApiException(ErrorCode.BAD_REQUEST, "No existing password hash set.")

        val verified = BCrypt.verifyer().verify(currentPassword.toCharArray(), hash).verified
        if (!verified) {
            throw ApiException(ErrorCode.UNAUTHORIZED, "Current password is incorrect.")
        }

        val newHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())
        val updated = repository.updatePassword(userId, newHash)
        if (!updated) {
            throw ApiException(ErrorCode.NOT_FOUND, "Failed to update password.")
        }

        return CommonResponse(success = true, message = "Password changed successfully.")
    }

    // ------------------------------------------------------------------
    // Forgot Password & Reset Password — OTP Flow (Resend)
    // ------------------------------------------------------------------

    suspend fun forgotPassword(email: String): CommonResponse {
        val normalizedEmail = email.trim().lowercase()
        val user = repository.findByEmail(normalizedEmail)
        if (user != null) {
            val code = generateSecureOtp()
            repository.saveOtp(normalizedEmail, code)
            resendEmailService?.sendPasswordResetOtp(normalizedEmail, code)
        }
        return CommonResponse(success = true, message = "OTP verification code sent to your email.")
    }

    suspend fun verifyOtp(email: String, code: String): CommonResponse {
        val normalizedEmail = email.trim().lowercase()
        val valid = repository.verifyAndConsumeOtp(normalizedEmail, code)
        if (valid) {
            return CommonResponse(
                success = true,
                message = "OTP verified successfully.",
            )
        } else {
            throw ApiException(ErrorCode.BAD_REQUEST, "Invalid or expired OTP code.")
        }
    }

    suspend fun resetPassword(email: String, code: String, newPassword: String): CommonResponse {
        val normalizedEmail = email.trim().lowercase()
        if (newPassword.length < 8) {
            throw ApiException(ErrorCode.BAD_REQUEST, "New password must be at least 8 characters.")
        }

        val valid = repository.verifyAndConsumeOtp(normalizedEmail, code)
        if (!valid) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Invalid or expired OTP code.")
        }

        val passwordHash = BCrypt.withDefaults().hashToString(12, newPassword.toCharArray())
        val updated = repository.updatePassword(normalizedEmail, passwordHash)
        if (!updated) {
            throw ApiException(ErrorCode.NOT_FOUND, "User account not found.")
        }

        return CommonResponse(success = true, message = "Password reset successfully. You can now log in.")
    }

    // ------------------------------------------------------------------
    // Email Verification (Resend)
    // ------------------------------------------------------------------

    suspend fun sendEmailVerification(userId: String): CommonResponse {
        val user = repository.findById(userId)
            ?: throw ApiException(ErrorCode.UNAUTHORIZED, "User not found.")

        val code = generateSecureOtp()
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
        val cleanedPhone = phoneNumber.trim()
        if (cleanedPhone.length < 8 || !cleanedPhone.replace("+", "").all { it.isDigit() }) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Invalid phone number format. Please provide E.164 format (e.g., +1234567890).")
        }

        val code = generateSecureOtp()
        repository.savePhoneOtp(userId, cleanedPhone, code)
        telnyxSmsService?.sendOtpSms(cleanedPhone, code)

        return CommonResponse(success = true, message = "SMS verification code dispatched to $cleanedPhone.")
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
