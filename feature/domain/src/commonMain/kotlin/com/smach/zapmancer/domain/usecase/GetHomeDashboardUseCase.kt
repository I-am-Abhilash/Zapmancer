package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.repository.HomeRepository

class GetHomeDashboardUseCase(
    private val repository: HomeRepository,
) {
    companion object {
        private const val MAX_RECENT_ACTIVITIES = 5
    }

    suspend operator fun invoke(): Result<HomeDashboard, DataError.Network> =
        when (val result = repository.getDashboardData()) {
            is Result.Success -> {
                // Business Rule: Home screen only needs the most recent activities.
                val optimized = result.data.copy(
                    recentActivities = result.data.recentActivities.take(MAX_RECENT_ACTIVITIES),
                )
                Result.Success(optimized)
            }

            is Result.Error -> result
        }
}
