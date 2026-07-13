package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.ExportActivitiesRequest
import com.smach.zapmancer.core.common.dto.ExportActivitiesResponse
import com.smach.zapmancer.core.common.dto.RecentActivity
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.domain.model.EarningStats
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStats
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
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
    ): Result<String, DataError.Network> = safeApiCall<ExportActivitiesResponse> {
        client.post("home/activities/export") {
            setBody(ExportActivitiesRequest(activities = activities.map { it.toDto() }))
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
        amount = totalEarnings.replace("$", "").replace(",", "").toDoubleOrNull() ?: 0.0,
        growthPercentage = earningsGrowth.replace("%", "").toDoubleOrNull() ?: 0.0,
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
            category = ProjectCategory.from(activity.category),
            tag = activity.categoryTag,
            status = runCatching { ActivityStatus.valueOf(activity.status) }
                .getOrDefault(ActivityStatus.IN_PROGRESS),
            timestamp = activity.date.toLongOrNull() ?: 0L,
            monetaryValue = activity.value,
        )
    },
    clientStats = null, // Set to null/default as it is client mode specific or not returned on default dashboard endpoint
)

private fun UserActivity.toDto(): RecentActivity = RecentActivity(
    id = id,
    projectName = projectName,
    category = category.name,
    categoryTag = tag,
    status = status.name,
    date = timestamp.toString(),
    value = monetaryValue,
)
