package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProfileRepository
import org.koin.core.annotation.Factory

@Factory
class GetUserReviewsUseCase(
    private val repository: ProfileRepository,
) {
    suspend operator fun invoke(
        userId: String,
        page: Int? = null,
        limit: Int? = null,
    ): Result<List<Review>, DataError.Network> =
        repository.getReviews(userId, page, limit)
}
