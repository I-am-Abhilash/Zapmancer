package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(request: UpdateProfileRequest): Result<UserProfile, DataError.Network> = repository.updateProfile(request)
}
