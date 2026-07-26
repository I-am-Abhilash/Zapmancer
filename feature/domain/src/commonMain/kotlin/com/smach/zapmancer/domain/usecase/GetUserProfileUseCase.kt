package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserProfile
import com.smach.zapmancer.domain.repository.ProfileRepository
import org.koin.core.annotation.Factory

@Factory
class GetUserProfileUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(userId: String? = null): Result<UserProfile, DataError.Network> = repository.getProfile(userId)
}
