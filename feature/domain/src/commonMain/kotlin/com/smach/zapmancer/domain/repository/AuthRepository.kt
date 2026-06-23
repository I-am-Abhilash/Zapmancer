package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun saveTokens(accessToken: String, refreshToken: String)

    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)

    suspend fun login(email: String, password: String): Result<User, DataError.Network>

    suspend fun signUp(
        email: String,
        username: String,
        password: String
    ): Result<User, DataError.Network>

    suspend fun requestPasswordReset(email: String): Result<Unit, DataError.Network>
    suspend fun verifyOtp(email: String, code: String): Result<Unit, DataError.Network>

    suspend fun logout()
}
