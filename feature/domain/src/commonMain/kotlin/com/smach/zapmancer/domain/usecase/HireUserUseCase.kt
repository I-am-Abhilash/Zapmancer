package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProfileRepository

class HireUserUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String): Result<Unit, DataError.Network> = repository.hireUser(userId)
}
