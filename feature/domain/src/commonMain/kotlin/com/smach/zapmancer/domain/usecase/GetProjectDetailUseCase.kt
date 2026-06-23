package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository

class GetProjectDetailUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String): Result<ProjectDetail> = try {
        Result.success(repository.getProjectDetail(id))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
