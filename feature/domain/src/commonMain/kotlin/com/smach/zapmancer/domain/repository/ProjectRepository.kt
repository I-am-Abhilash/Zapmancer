package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectDetail

interface ProjectRepository {
    suspend fun getProjects(): List<Project>
    suspend fun getProjectDetail(id: String): ProjectDetail
    suspend fun saveProject(id: String, isSaved: Boolean)
    suspend fun applyForProject(id: String)
    suspend fun postProject(project: ProjectDetail): com.smach.zapmancer.core.common.utils.Result<Unit, com.smach.zapmancer.core.common.utils.DataError.Network>
}
