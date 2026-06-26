package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Authentication + onboarding contract.
 *
 * Every method that can fail returns [Result] with [DataError.Network] — no method throws.
 * Methods whose only failure mode is local (saving tokens to DataStore) return [Result]
 * too, so callers have one consistent shape to handle.
 */
interface AuthRepository {
    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit, DataError.Network>

    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean): Result<Unit, DataError.Network>

    suspend fun login(email: String, password: String): Result<User, DataError.Network>

    suspend fun signUp(
        email: String,
        username: String,
        password: String,
    ): Result<User, DataError.Network>

    suspend fun requestPasswordReset(email: String): Result<Unit, DataError.Network>
    suspend fun verifyOtp(email: String, code: String): Result<Unit, DataError.Network>

    suspend fun logout(): Result<Unit, DataError.Network>
}
