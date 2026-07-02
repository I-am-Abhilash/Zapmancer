package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.*
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.core.network.session.SessionManager
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

class AuthRepositoryImpl(
    private val client: HttpClient,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override fun isOnboardingCompleted(): Flow<Boolean> = sessionManager.getOnboardingCompleted()

    override suspend fun setOnboardingCompleted(completed: Boolean): Result<Unit, DataError.Network> = runCatching { sessionManager.saveOnboardingCompleted(completed) }
        .fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(DataError.Network.UNKNOWN, it) },
        )

    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit, DataError.Network> = runCatching { sessionManager.saveTokens(accessToken, refreshToken) }
        .fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(DataError.Network.UNKNOWN, it) },
        )

    override suspend fun login(email: String, password: String): Result<User, DataError.Network> {
        val result = safeApiCall<User> {
            client.post("auth/login") {
                setBody(LoginRequest(email = email, password = password))
            }
        }
        if (result is Result.Success) persistSession(result.data)
        return result
    }

    override suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): Result<User, DataError.Network> {
        val result = safeApiCall<User> {
            client.post("auth/register") {
                setBody(SignUpRequest(email = email, username = username, password = password))
            }
        }
        if (result is Result.Success) persistSession(result.data)
        return result
    }

    override suspend fun requestPasswordReset(email: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("auth/forgot-password") {
            setBody(ForgotPasswordRequest(email = email))
        }
    }.toUnitResult()

    override suspend fun verifyOtp(email: String, code: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("auth/verify-otp") {
            setBody(VerifyOtpRequest(email = email, code = code))
        }
    }.toUnitResult()

    override suspend fun logout(): Result<Unit, DataError.Network> = runCatching { sessionManager.clearSession() }
        .fold(
            onSuccess = { Result.Success(Unit) },
            onFailure = { Result.Error(DataError.Network.UNKNOWN, it) },
        )

    private suspend fun persistSession(user: User) {
        sessionManager.saveSession(
            userId = user.id,
            accessToken = user.accessToken ?: "",
            refreshToken = user.refreshToken ?: "",
        )
    }
}
