package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository

class PostProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(project: ProjectDetail): Result<Unit, DataError.Network> {
        return repository.postProject(project)
    }
}
