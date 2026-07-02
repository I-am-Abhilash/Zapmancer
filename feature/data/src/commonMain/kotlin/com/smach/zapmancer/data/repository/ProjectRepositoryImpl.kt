package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
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
    private val client: HttpClient,
) : ProjectRepository {

    override suspend fun getProjects(): Result<List<Project>, DataError.Network> = safeApiCall<List<Project>> { client.get("projects") }

    override suspend fun getProjectDetail(id: String): Result<ProjectDetail, DataError.Network> = safeApiCall<ProjectDetail> { client.get("projects/$id") }

    override suspend fun saveProject(id: String, isSaved: Boolean): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("projects/$id/save") {
            setBody(SaveProjectRequest(isSaved = isSaved))
        }
    }.toUnitResult()

    override suspend fun applyForProject(id: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> { client.post("projects/$id/apply") }.toUnitResult()

    override suspend fun postProject(project: ProjectDetail): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("projects") {
            setBody(project)
        }
    }.toUnitResult()
}

@Serializable
private data class SaveProjectRequest(val isSaved: Boolean)
