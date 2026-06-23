package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository

class ExportActivityCsvUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(activities: List<UserActivity>): Result<String> = try {
        if (activities.isEmpty()) {
            Result.failure(Exception("No activities to export"))
        } else {
            val filePath = repository.exportActivitiesToCsv(activities)
            Result.success(filePath)
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
