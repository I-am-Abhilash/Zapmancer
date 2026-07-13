package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.repository.ProjectRepository

class GetProjectsUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(
        query: String? = null,
        category: ProjectCategory? = null,
        sortBy: String? = null,
        page: Int? = null,
        pageSize: Int? = null,
    ): Result<List<Project>, DataError.Network> = repository.getProjects(query, category, sortBy, page, pageSize)
}
