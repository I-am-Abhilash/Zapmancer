package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProfileRepository
import org.koin.core.annotation.Factory

@Factory
class SubmitUserReviewUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        userId: String,
        content: String,
        rating: Int,
    ): Result<Unit, DataError.Network> = repository.submitReview(userId, content, rating)
}
