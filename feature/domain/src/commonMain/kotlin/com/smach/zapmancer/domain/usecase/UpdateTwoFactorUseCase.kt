package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.SettingsRepository
import org.koin.core.annotation.Factory

@Factory
class UpdateTwoFactorUseCase(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit, DataError.Network> = repository.updateTwoFactor(enabled)
}
