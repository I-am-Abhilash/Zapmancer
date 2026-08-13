package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetDarkModeUseCase(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<Boolean> = repository.darkModeFlow
}
