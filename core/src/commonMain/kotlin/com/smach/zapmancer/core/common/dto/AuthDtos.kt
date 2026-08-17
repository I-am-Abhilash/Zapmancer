package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class SignUpRequest(val email: String, val username: String, val password: String)

@Serializable
data class ForgotPasswordRequest(val email: String)

@Serializable
data class VerifyOtpRequest(val email: String, val code: String)

@Serializable
data class RefreshTokenRequest(val refreshToken: String)

@Serializable
data class AuthResponse(
    val id: String,
    val email: String?,
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean = false,
)

@Serializable
data class SendPhoneOtpRequest(val phoneNumber: String)

@Serializable
data class VerifyPhoneOtpRequest(val code: String)

@Serializable
data class SendEmailVerificationRequest(val email: String? = null)

@Serializable
data class VerifyEmailRequest(val code: String)

@Serializable
data class VerificationStatusResponse(
    val isEmailVerified: Boolean,
    val isPhoneVerified: Boolean,
    val isIdentityVerified: Boolean,
    val email: String?,
    val phoneNumber: String?,
)
