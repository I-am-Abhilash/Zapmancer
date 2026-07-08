package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.domain.model.EarningStats
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.ProjectStats
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable
import com.smach.zapmancer.core.common.dto.HomeDashboard as HomeDashboardDto

class HomeRepositoryImpl(
    private val client: HttpClient,
) : HomeRepository {

    override suspend fun getDashboardData(): Result<HomeDashboard, DataError.Network> = safeApiCall<HomeDashboardDto> {
        client.get("home/dashboard")
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> result
        }
    }

    override suspend fun exportActivitiesToCsv(
        activities: List<UserActivity>,
    ): Result<String, DataError.Network> = safeApiCall<ExportResponse> {
        client.post("home/activities/export") {
            setBody(ExportRequest(activities = activities))
        }
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.filePath)
            is Result.Error -> result
        }
    }
}

/** Maps the flat server wire-format DTO → nested domain model. */
private fun HomeDashboardDto.toDomain(): HomeDashboard = HomeDashboard(
    userName = userName,
    earnings = EarningStats(
        amount = totalEarnings,
        growthPercentage = earningsGrowth,
    ),
    projectStats = ProjectStats(
        activeCount = activeProjectsCount,
        capacity = totalCapacity,
    ),
    systemRating = systemRating,
    recentActivities = recentActivities.map { activity ->
        UserActivity(
            id = activity.id,
            projectName = activity.projectName,
            category = activity.category,
            tag = activity.categoryTag,
            status = runCatching { ActivityStatus.valueOf(activity.status) }
                .getOrDefault(ActivityStatus.IN_PROGRESS),
            timestamp = activity.date,
            monetaryValue = activity.value,
        )
    },
)

@Serializable
private data class ExportRequest(val activities: List<UserActivity>)

@Serializable
private data class ExportResponse(val filePath: String)
