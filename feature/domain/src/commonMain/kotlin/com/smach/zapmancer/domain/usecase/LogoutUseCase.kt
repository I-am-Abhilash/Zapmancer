package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.SettingsRepository

class LogoutUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(): Result<Unit> = try {
        repository.logout()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
