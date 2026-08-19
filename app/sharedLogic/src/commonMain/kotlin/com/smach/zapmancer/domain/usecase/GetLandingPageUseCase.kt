package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.LandingPageData
import com.smach.zapmancer.domain.repository.LandingPageRepository
import org.koin.core.annotation.Factory

@Factory
class GetLandingPageUseCase(
    private val repository: LandingPageRepository,
) {
    suspend operator fun invoke(): Result<LandingPageData, DataError.Network> = repository.getLandingPageData()
}
