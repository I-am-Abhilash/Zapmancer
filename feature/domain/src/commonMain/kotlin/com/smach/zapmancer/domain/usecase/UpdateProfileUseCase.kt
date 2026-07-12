package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UpdateProfileParams
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(params: UpdateProfileParams): Result<UserProfile, DataError> {
        if (params.name?.isBlank() == true) {
            return Result.Error(DataError.Local.INVALID_INPUT)
        }
        return repository.updateProfile(params)
    }
}
