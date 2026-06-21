package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository
) {
    suspend fun updateTwoFactor(enabled: Boolean): Result<Unit> {
        return try {
            repository.updateTwoFactor(enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateDarkMode(enabled: Boolean): Result<Unit> {
        return try {
            repository.updateDarkMode(enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateEmailNotifications(enabled: Boolean): Result<Unit> {
        return try {
            repository.updateEmailNotifications(enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateClientMode(enabled: Boolean): Result<Unit> {
        return try {
            repository.updateClientMode(enabled)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
