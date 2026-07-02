package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.SettingsRepository

class UpdateSettingsUseCase(
    private val repository: SettingsRepository,
) {
    suspend fun updateTwoFactor(enabled: Boolean): Result<Unit, DataError.Network> = repository.updateTwoFactor(enabled)

    suspend fun updateDarkMode(enabled: Boolean): Result<Unit, DataError.Network> = repository.updateDarkMode(enabled)

    suspend fun updateEmailNotifications(enabled: Boolean): Result<Unit, DataError.Network> = repository.updateEmailNotifications(enabled)

    suspend fun updateClientMode(enabled: Boolean): Result<Unit, DataError.Network> = repository.updateClientMode(enabled)
}
