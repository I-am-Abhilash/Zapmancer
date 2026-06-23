package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.ProjectRepository

class ApplyProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit> = try {
        repository.applyForProject(id)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
