package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository
import org.koin.core.annotation.Factory

@Factory
class ExportActivityCsvUseCase(
    private val repository: HomeRepository,
) {
    suspend operator fun invoke(activities: List<UserActivity>): Result<String, DataError> {
        if (activities.isEmpty()) {
            return Result.Error(DataError.Local.INVALID_INPUT)
        }
        return repository.exportActivitiesToCsv(activities)
    }
}
