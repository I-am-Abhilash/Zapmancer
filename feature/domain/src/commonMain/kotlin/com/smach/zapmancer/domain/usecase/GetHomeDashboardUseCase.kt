package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.repository.HomeRepository

class GetHomeDashboardUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(): Result<HomeDashboard> = try {
        val data = repository.getDashboardData()
        // Business Rule: Home screen only needs the 5 most recent activities
        val optimizedData = data.copy(
            recentActivities = data.recentActivities.take(5),
        )
        Result.success(optimizedData)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
