package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository

class ExportActivityCsvUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(activities: List<UserActivity>): Result<String, DataError.Network> {
        if (activities.isEmpty()) {
            return Result.Error(DataError.Network.CLIENT_ERROR)
        }
        return repository.exportActivitiesToCsv(activities)
    }
}
