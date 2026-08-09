package com.smach.zapmancer.projects.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.core.recommendations.GorseClient
import com.smach.zapmancer.projects.repository.ProjectsRepository
import org.koin.core.annotation.Single


@Single
class ProjectsService(
    private val repository: ProjectsRepository,
    private val gorseClient: GorseClient,
) {

    suspend fun getProjects(
        userId: String,
        query: String? = null,
        category: String? = null,
        sortBy: String? = null,
        page: Int? = null,
        limit: Int? = null,
    ): List<Project> = repository.getAllProjects(
        userId = userId,
        query = query,
        category = category,
        sortBy = sortBy,
        page = page,
        limit = limit,
    )

    suspend fun getProjectById(projectId: String, userId: String): ProjectDetail {
        return repository.findById(projectId, userId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Project not found.")
    }

    suspend fun saveProject(
        userId: String,
        projectId: String,
        save: Boolean,
    ): CommonResponse {
        repository.toggleSave(userId, projectId, save)
        val msg = if (save) "Project saved successfully." else "Project removed from saved."
        return CommonResponse(success = true, message = msg)
    }

    suspend fun applyToProject(userId: String, projectId: String): CommonResponse {
        repository.findById(projectId, userId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Project not found.")
        repository.apply(userId, projectId)
        gorseClient.insertFeedback("apply", userId, projectId)
        return CommonResponse(
            success = true,
            message = "Application submitted successfully.",
        )
    }

    suspend fun createProject(
        clientId: String,
        request: CreateProjectRequest,
    ): CommonResponse {
        val projectId = repository.create(clientId, request)
        gorseClient.insertItem(projectId)
        return CommonResponse(
            success = true,
            message = "Project created successfully.",
        )
    }
}

