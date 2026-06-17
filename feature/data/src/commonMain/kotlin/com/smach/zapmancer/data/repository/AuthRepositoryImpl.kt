package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.core.network.session.SessionManager
import com.smach.zapmancer.data.mapper.toDomain
import com.smach.zapmancer.data.model.LoginRequestDto
import com.smach.zapmancer.data.model.SignupRequestDto
import com.smach.zapmancer.data.model.UserDto
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.repository.AuthRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.coroutines.flow.Flow

class AuthRepositoryImpl(
    private val httpClient: HttpClient,
    private val sessionManager: SessionManager,
) : AuthRepository {

    override fun getUserId(): Flow<String?> = sessionManager.getUserId()

    override fun getAccessToken(): Flow<String?> = sessionManager.getAccessToken()

    override fun getRefreshToken(): Flow<String?> = sessionManager.getRefreshToken()

    override suspend fun saveTokens(accessToken: String, refreshToken: String) {
        sessionManager.saveTokens(accessToken, refreshToken)
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Result<User, DataError.Network> {
        val result = safeApiCall<UserDto> {
            httpClient.post("auth/login") {
                setBody(LoginRequestDto(email, password))
            }
        }
        return when (result) {
            is Result.Success -> handleAuthSuccess(result.data)

            is Result.Error -> result
        }
    }

    override suspend fun signUp(
        email: String,
        username : String,
        password: String,
    ): Result<User, DataError.Network> {
        val result = safeApiCall<UserDto> {
            httpClient.post("auth/signup") {
                setBody(SignupRequestDto(email, username , password))
            }
        }
        return when (result) {
            is Result.Success -> handleAuthSuccess(result.data)

            is Result.Error -> result
        }
    }
    private suspend fun handleAuthSuccess(
        dto: UserDto
    ): Result<User, DataError.Network> {
        val id = dto.id
        val access = dto.accessToken
        val refresh = dto.refreshToken

        return if (access != null && refresh != null) {
            sessionManager.saveSession(
                userId = id,
                accessToken = access,
                refreshToken = refresh
            )

            Result.Success(dto.toDomain())
        } else {
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun logout() {
        sessionManager.clearSession()
    }
}
