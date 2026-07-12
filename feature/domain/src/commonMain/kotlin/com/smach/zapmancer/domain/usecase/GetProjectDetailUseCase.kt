package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository

class GetProjectDetailUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String): Result<ProjectDetail, DataError.Network> =
        repository.getProjectDetail(id)
}
