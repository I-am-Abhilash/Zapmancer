package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.DataError
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

    override suspend fun getDashboardData(): Result<HomeDashboard, DataError.Network> =
        safeApiCall<HomeDashboard> { client.get("home/dashboard") }

    override suspend fun exportActivitiesToCsv(activities: List<UserActivity>): Result<String, DataError.Network> =
        safeApiCall<ExportResponse> {
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

@Serializable
private data class ExportRequest(val activities: List<UserActivity>)

@Serializable
private data class ExportResponse(val filePath: String)
