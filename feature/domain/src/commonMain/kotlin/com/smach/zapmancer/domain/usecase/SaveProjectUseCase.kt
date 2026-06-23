package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.repository.ProjectRepository

class SaveProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(id: String, isSaved: Boolean): Result<Unit> = try {
        repository.saveProject(id, isSaved)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
