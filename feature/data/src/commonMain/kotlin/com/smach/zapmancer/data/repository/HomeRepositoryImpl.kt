package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import kotlinx.serialization.Serializable

class HomeRepositoryImpl(
    private val client: HttpClient,
) : HomeRepository {

    override suspend fun getDashboardData(): HomeDashboard = when (
        val result = safeApiCall<HomeDashboard> {
            client.get("home/dashboard")
        }
    ) {
        is Result.Success -> result.data
        is Result.Error -> throw Exception("Failed to load dashboard: ${result.error}")
    }

    override suspend fun exportActivitiesToCsv(activities: List<UserActivity>): String = when (
        val result = safeApiCall<ExportResponse> {
            client.post("home/activities/export") {
                setBody(ExportRequest(activities = activities))
            }
        }
    ) {
        is Result.Success -> result.data.filePath
        is Result.Error -> "activities_export.csv" // fallback local path
    }
}

@Serializable
private data class ExportRequest(val activities: List<UserActivity>)

@Serializable
private data class ExportResponse(val filePath: String)
