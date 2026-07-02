package com.smach.zapmancer.feature.api.auth

import kotlinx.serialization.Serializable

// ---------------------------------------------------------------------------
// Requests
// ---------------------------------------------------------------------------

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

// ---------------------------------------------------------------------------
// Responses
// ---------------------------------------------------------------------------

/**
 * Standard auth response returned on login, register, and token refresh.
 * Maps directly to the `User` object expected by AuthRepositoryImpl in the KMP app.
 */
@Serializable
data class AuthResponse(
    val id: String,
    val email: String?,
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean = false,
)
