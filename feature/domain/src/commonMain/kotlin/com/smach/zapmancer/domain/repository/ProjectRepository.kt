package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectDetail

interface ProjectRepository {
    suspend fun getProjects(): Result<List<Project>, DataError.Network>
    suspend fun getProjectDetail(id: String): Result<ProjectDetail, DataError.Network>
    suspend fun saveProject(id: String, isSaved: Boolean): Result<Unit, DataError.Network>
    suspend fun applyForProject(id: String): Result<Unit, DataError.Network>
    suspend fun postProject(project: ProjectDetail): Result<Unit, DataError.Network>
}
