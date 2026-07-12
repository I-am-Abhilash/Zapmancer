package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetDarkModeUseCase(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.darkModeFlow
}
