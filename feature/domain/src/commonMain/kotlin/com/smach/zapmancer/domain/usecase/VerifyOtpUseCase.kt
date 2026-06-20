package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.AuthRepository

class VerifyOtpUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, code: String): Result<Unit, DataError.Network> {
        return repository.verifyOtp(email, code)
    }
}
