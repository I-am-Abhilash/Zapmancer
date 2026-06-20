package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.repository.ProjectRepository

class GetProjectsUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): Result<List<Project>> {
        return try {
            Result.success(repository.getProjects())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
