package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.CreateProjectParams
import com.smach.zapmancer.domain.repository.ProjectRepository
import org.koin.core.annotation.Factory

@Factory
class PostProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(params: CreateProjectParams): Result<Unit, DataError> {
        if (params.title.isBlank() || params.description.isBlank()) {
            return Result.Error(DataError.Local.INVALID_INPUT)
        }
        return repository.postProject(params)
    }
}
