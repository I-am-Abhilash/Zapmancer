package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.SettingsData
import com.smach.zapmancer.domain.repository.SettingsRepository

class GetSettingsUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(): Result<SettingsData> = try {
        Result.success(repository.getSettings())
    } catch (e: Exception) {
        Result.failure(e)
    }
}
