package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.ForgotPasswordRequest
import com.smach.zapmancer.core.common.dto.LoginRequest
import com.smach.zapmancer.core.common.dto.SignUpRequest
import com.smach.zapmancer.core.common.dto.VerifyOtpRequest
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

    /**
     * Hits the server's POST /auth/logout first; on success the local session is cleared.
     * A network failure still attempts the local clear so the user is never stuck signed in
     * on the device when the server is unreachable — the next login will mint a fresh token.
     */
    override suspend fun logout(): Result<Unit, DataError.Network> {
        val networkResult = safeApiCall<CommonResponse> { client.post("auth/logout") }
        return runCatching { sessionManager.clearSession() }
            .fold(
                onSuccess = {
                    when (networkResult) {
                        is Result.Success -> Result.Success(Unit)
                        is Result.Error -> networkResult
                    }
                },
                onFailure = { Result.Error(DataError.Network.UNKNOWN, it) },
            )
    }

    private suspend fun persistSession(user: User) {
        sessionManager.saveSession(
            userId = user.id,
            accessToken = user.accessToken ?: "",
            refreshToken = user.refreshToken ?: "",
        )
    }
}
