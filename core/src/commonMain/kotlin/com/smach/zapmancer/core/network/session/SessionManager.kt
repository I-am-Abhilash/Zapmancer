package com.smach.zapmancer.core.network.session

import com.smach.zapmancer.core.datastore.DataStoreStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single

@Single
class SessionManager(
    private val storage: DataStoreStorage,
) {
    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_ONBOARDING = "onboarding_completed"
    }

    suspend fun saveSession(userId: String, accessToken: String, refreshToken: String) {
        storage.saveString(KEY_USER_ID, userId)
        storage.saveString(KEY_ACCESS_TOKEN, accessToken)
        storage.saveString(KEY_REFRESH_TOKEN, refreshToken)
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        storage.saveString(KEY_ACCESS_TOKEN, accessToken)
        storage.saveString(KEY_REFRESH_TOKEN, refreshToken)
    }

    fun observeAccessToken(): Flow<String?> = storage.getString(KEY_ACCESS_TOKEN)

    suspend fun getUserId(): String? = storage.getString(KEY_USER_ID).first()
    suspend fun getAccessToken(): String? = storage.getString(KEY_ACCESS_TOKEN).first()

    suspend fun getRefreshToken(): String? = storage.getString(KEY_REFRESH_TOKEN).first()

    fun getOnboardingCompleted(): Flow<Boolean> = storage.getString(KEY_ONBOARDING).map { it == "true" }

    suspend fun saveOnboardingCompleted(completed: Boolean) {
        storage.saveString(KEY_ONBOARDING, completed.toString())
    }

    suspend fun clearSession() {
        storage.saveString(KEY_USER_ID, "")
        storage.saveString(KEY_ACCESS_TOKEN, "")
        storage.saveString(KEY_REFRESH_TOKEN, "")
    }
}
