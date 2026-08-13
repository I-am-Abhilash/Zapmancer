package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.AuthRepository
import org.koin.core.annotation.Factory

@Factory
class ForgotPasswordUseCase(
    private val repository: AuthRepository,
) {
    suspend operator fun invoke(email: String): Result<Unit, DataError.Network> = repository.requestPasswordReset(email)
}
