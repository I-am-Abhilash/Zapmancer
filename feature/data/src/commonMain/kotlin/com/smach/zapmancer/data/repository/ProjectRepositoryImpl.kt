package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.SaveProjectRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.CreateProjectParams
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.repository.ProjectRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import com.smach.zapmancer.core.common.dto.Project as ProjectDto
import com.smach.zapmancer.core.common.dto.ProjectDetail as ProjectDetailDto

class ProjectRepositoryImpl(
    private val client: HttpClient,
) : ProjectRepository {

    override suspend fun getProjects(
        query: String?,
        category: ProjectCategory?,
        sortBy: String?,
        page: Int?,
        pageSize: Int?,
    ): Result<List<Project>, DataError.Network> = safeApiCall<List<ProjectDto>> {
        client.get("projects") {
            url {
                query?.let { parameters.append("query", it) }
                category?.let { parameters.append("category", it.name) }
                sortBy?.let { parameters.append("sortBy", it) }
                page?.let { parameters.append("page", it.toString()) }
                pageSize?.let { parameters.append("pageSize", it.toString()) }
            }
        }
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            is Result.Error -> result
        }
    }

    override suspend fun getProjectDetail(id: String): Result<ProjectDetail, DataError.Network> = safeApiCall<ProjectDetailDto> {
        client.get("projects/$id")
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> result
        }
    }

    override suspend fun saveProject(id: String, isSaved: Boolean): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("projects/$id/save") {
            setBody(SaveProjectRequest(isSaved = isSaved))
        }
    }.toUnitResult()

    override suspend fun applyForProject(id: String): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> { client.post("projects/$id/apply") }.toUnitResult()

    override suspend fun postProject(params: CreateProjectParams): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("projects") {
            setBody(
                CreateProjectRequest(
                    category = params.category,
                    title = params.title,
                    location = "Remote",
                    budgetRange = params.budgetRange,
                    projectType = "Fixed Price",
                    projectScope = params.description,
                    deliverables = params.deliverables,
                    skills = params.skills,
                    timeline = params.timeline,
                    estStart = null,
                ),
            )
        }
    }.toUnitResult()
}

private fun ProjectDto.toDomain(): Project = Project(
    id = id,
    category = com.smach.zapmancer.domain.model.ProjectCategory.from(category),
    status = com.smach.zapmancer.domain.model.ProjectStatus.ACTIVE,
    title = title,
    description = "$projectType project located in $location. Budget: $budgetRange.",
    progress = null,
    tags = skills,
    postedTime = postedTime,
    membersCount = 0,
)

private fun ProjectDetailDto.toDomain(): ProjectDetail = ProjectDetail(
    id = id,
    category = category,
    title = title,
    postedTime = postedTime,
    location = location,
    isPaymentVerified = isPaymentVerified,
    projectScope = projectScope,
    deliverables = deliverables,
    skills = skills,
    budgetRange = budgetRange,
    projectType = projectType,
    timeline = timeline,
    estStart = estStart,
    clientName = clientName,
    clientIndustry = clientIndustry,
    clientLocation = clientLocation,
    clientProjectsCount = clientProjectsCount,
    clientRating = clientRating,
    isSaved = isSaved,
    isClientActive = isClientActive,
    isIdentityVerified = isIdentityVerified,
    isPhoneVerified = isPhoneVerified,
)
