package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.core.network.session.SessionManager
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val ONBOARDING_KEY = "onboarding_completed"

    override fun getUserId(): Flow<String?> = sessionManager.getUserId()
    override fun getAccessToken(): Flow<String?> = sessionManager.getAccessToken()
    override fun getRefreshToken(): Flow<String?> = sessionManager.getRefreshToken()

    override fun isOnboardingCompleted(): Flow<Boolean> =
        sessionManager.getOnboardingCompleted()

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        sessionManager.saveOnboardingCompleted(completed)
    }

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        sessionManager.saveTokens(accessToken, refreshToken)
    }

    override suspend fun login(email: String, password: String): Result<User, DataError.Network> {
        val result = safeApiCall<User> {
            client.post("auth/login") {
                setBody(LoginRequest(email = email, password = password))
            }
        }
        if (result is Result.Success) {
            val user = result.data
            sessionManager.saveSession(
                userId = user.id,
                accessToken = user.accessToken ?: "",
                refreshToken = user.refreshToken ?: ""
            )
        }
        return result
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Result<User, DataError.Network> {
        val result = safeApiCall<User> {
            client.post("auth/register") {
                setBody(SignUpRequest(email = email, username = username, password = password))
            }
        }
        if (result is Result.Success) {
            val user = result.data
            sessionManager.saveSession(
                userId = user.id,
                accessToken = user.accessToken ?: "",
                refreshToken = user.refreshToken ?: ""
            )
        }
        return result
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit, DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("auth/forgot-password") {
                setBody(ForgotPasswordRequest(email = email))
            }
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun verifyOtp(email: String, code: String): Result<Unit, DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("auth/verify-otp") {
                setBody(VerifyOtpRequest(email = email, code = code))
            }
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }
}

@Serializable
private data class LoginRequest(val email: String, val password: String)

@Serializable
private data class SignUpRequest(val email: String, val username: String, val password: String)

@Serializable
private data class ForgotPasswordRequest(val email: String)

@Serializable
private data class VerifyOtpRequest(val email: String, val code: String)
