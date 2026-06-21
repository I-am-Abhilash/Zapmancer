package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

class ProjectRepositoryImpl(
    private val client: HttpClient
) : ProjectRepository {

    override suspend fun getProjects(): List<Project> {
        return when (val result = safeApiCall<List<Project>> {
            client.get("projects")
        }) {
            is Result.Success -> result.data
            is Result.Error -> throw Exception("Failed to fetch projects: ${result.error}")
        }
    }

    override suspend fun getProjectDetail(id: String): ProjectDetail {
        return when (val result = safeApiCall<ProjectDetail> {
            client.get("projects/$id")
        }) {
            is Result.Success -> result.data
            is Result.Error -> throw Exception("Failed to fetch project detail: ${result.error}")
        }
    }

    override suspend fun saveProject(id: String, isSaved: Boolean) {
        val result = safeApiCall<CommonResponse> {
            client.post("projects/$id/save") {
                setBody(SaveProjectRequest(isSaved = isSaved))
            }
        }
        if (result is Result.Error) {
            throw Exception("Failed to save project: ${result.error}")
        }
    }

    override suspend fun applyForProject(id: String) {
        val result = safeApiCall<CommonResponse> {
            client.post("projects/$id/apply")
        }
        if (result is Result.Error) {
            throw Exception("Failed to apply for project: ${result.error}")
        }
    }

    override suspend fun postProject(project: ProjectDetail): Result<Unit, com.smach.zapmancer.core.common.utils.DataError.Network> {
        val result = safeApiCall<CommonResponse> {
            client.post("projects") {
                setBody(project)
            }
        }
        return when (result) {
            is Result.Success -> Result.Success(Unit)
            is Result.Error -> Result.Error(result.error)
        }
    }
}

@Serializable
private data class SaveProjectRequest(val isSaved: Boolean)
