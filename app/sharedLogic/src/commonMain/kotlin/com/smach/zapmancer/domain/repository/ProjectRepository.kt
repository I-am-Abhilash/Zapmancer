package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.CreateProjectParams
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectDetail

interface ProjectRepository {
    suspend fun getProjects(
        query: String? = null,
        category: ProjectCategory? = null,
        sortBy: String? = null,
        page: Int? = null,
        pageSize: Int? = null,
    ): Result<List<Project>, DataError.Network>

    suspend fun getProjectDetail(id: String): Result<ProjectDetail, DataError.Network>
    suspend fun getMyProjects(): Result<List<Project>, DataError.Network>
    suspend fun saveProject(id: String, isSaved: Boolean): Result<Unit, DataError.Network>
    suspend fun applyForProject(id: String): Result<Unit, DataError.Network>
    suspend fun postProject(params: CreateProjectParams): Result<Unit, DataError.Network>
    suspend fun updateProject(id: String, params: CreateProjectParams): Result<Project, DataError.Network>
    suspend fun deleteProject(id: String): Result<Unit, DataError.Network>
    suspend fun updateProjectStatus(id: String, status: String): Result<Unit, DataError.Network>
}
