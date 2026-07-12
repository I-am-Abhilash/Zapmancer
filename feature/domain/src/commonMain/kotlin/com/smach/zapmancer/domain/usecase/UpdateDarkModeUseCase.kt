package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.SettingsRepository

class UpdateDarkModeUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean) = repository.updateDarkMode(enabled)
}
