package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProfileRepository
import org.koin.core.annotation.Factory

@Factory
class HireUserUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String): Result<Unit, DataError> {
        if (userId.isBlank()) {
            return Result.Error(DataError.Local.INVALID_INPUT)
        }
        return repository.hireUser(userId)
    }
}
