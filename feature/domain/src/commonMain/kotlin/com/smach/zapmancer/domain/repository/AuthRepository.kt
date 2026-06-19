package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getUserId(): Flow<String?>
    fun getAccessToken(): Flow<String?>
    fun getRefreshToken(): Flow<String?>
    suspend fun saveTokens(accessToken: String, refreshToken: String)

    suspend fun login(email: String, password: String): Result<User, DataError.Network>

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Result<User, DataError.Network>

    suspend fun logout()
}
