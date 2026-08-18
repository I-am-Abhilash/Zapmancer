package com.smach.zapmancer.projects.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.core.common.dto.UpdateProjectRequest
import com.smach.zapmancer.projects.repository.ProjectsRepository
import org.koin.core.annotation.Single

private val VALID_PROJECT_STATUSES = setOf("OPEN", "IN_PROGRESS", "COMPLETED", "CLOSED")

@Single
class ProjectsService(
    private val repository: ProjectsRepository,
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

    suspend fun getRecommendedProjects(userId: String, limit: Int = 10): List<Project> {
        return repository.getRecommendedProjects(userId = userId, limit = limit)
    }

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
        return CommonResponse(
            success = true,
            message = "Application submitted successfully.",
        )
    }

    suspend fun getMyProjects(clientId: String): List<Project> {
        return repository.getMyProjects(clientId)
    }

    suspend fun createProject(
        clientId: String,
        request: CreateProjectRequest,
    ): CommonResponse {
        if (request.title.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Project title cannot be blank.")
        }
        if (request.category.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Project category cannot be blank.")
        }
        if (request.budgetRange.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Budget range cannot be blank.")
        }

        repository.create(clientId, request)
        return CommonResponse(
            success = true,
            message = "Project created successfully.",
        )
    }

    suspend fun updateProject(
        clientId: String,
        projectId: String,
        request: UpdateProjectRequest,
    ): CommonResponse {
        val updated = repository.update(projectId, clientId, request)
        if (!updated) {
            throw ApiException(ErrorCode.FORBIDDEN, "Project not found or you are not authorized to edit it.")
        }
        return CommonResponse(
            success = true,
            message = "Project updated successfully.",
        )
    }

    suspend fun updateProjectStatus(
        clientId: String,
        projectId: String,
        newStatus: String,
    ): CommonResponse {
        val normalizedStatus = newStatus.trim().uppercase()
        if (normalizedStatus !in VALID_PROJECT_STATUSES) {
            throw ApiException(
                ErrorCode.BAD_REQUEST,
                "Invalid project status '$newStatus'. Allowed values: ${VALID_PROJECT_STATUSES.joinToString(", ")}."
            )
        }

        val updated = repository.updateStatus(projectId, clientId, normalizedStatus)
        if (!updated) {
            throw ApiException(ErrorCode.FORBIDDEN, "Project not found or you are not authorized to update its status.")
        }

        return CommonResponse(
            success = true,
            message = "Project status transitioned to $normalizedStatus.",
        )
    }

    suspend fun deleteProject(
        clientId: String,
        projectId: String,
    ): CommonResponse {
        val deleted = repository.delete(projectId, clientId)
        if (!deleted) {
            throw ApiException(ErrorCode.FORBIDDEN, "Project not found or you are not authorized to delete it.")
        }
        return CommonResponse(
            success = true,
            message = "Project deleted successfully.",
        )
    }
}
