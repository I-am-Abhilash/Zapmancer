package com.smach.zapmancer.projects.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.ErrorCode
import com.smach.zapmancer.projects.api.CreateProjectRequest
import com.smach.zapmancer.projects.api.Project
import com.smach.zapmancer.projects.api.ProjectDetail
import com.smach.zapmancer.projects.data.ProjectsRepository
import com.smach.zapmancer.recommendations.GorseClient

class ProjectsService(
    private val repository: ProjectsRepository,
    private val gorseClient: GorseClient
) {

    suspend fun getProjects(userId: String): DomainResult<List<Project>> =
        DomainResult.Success(repository.getAllProjects(userId))

    suspend fun getProjectById(projectId: String, userId: String): DomainResult<ProjectDetail> {
        val detail = repository.findById(projectId, userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "Project not found.")
        return DomainResult.Success(detail)
    }

    suspend fun saveProject(userId: String, projectId: String, save: Boolean): DomainResult<CommonResponse> {
        repository.toggleSave(userId, projectId, save)
        val msg = if (save) "Project saved successfully." else "Project removed from saved."
        return DomainResult.Success(CommonResponse(success = true, message = msg))
    }

    suspend fun applyToProject(userId: String, projectId: String): DomainResult<CommonResponse> {
        val detail = repository.findById(projectId, userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "Project not found.")
        repository.apply(userId, projectId)
        gorseClient.insertFeedback("apply", userId, projectId)
        return DomainResult.Success(CommonResponse(success = true, message = "Application submitted successfully."))
    }

    suspend fun createProject(clientId: String, request: CreateProjectRequest): DomainResult<CommonResponse> {
        val projectId = repository.create(clientId, request)
        gorseClient.insertItem(projectId)
        return DomainResult.Success(CommonResponse(success = true, message = "Project created successfully."))
    }
}
