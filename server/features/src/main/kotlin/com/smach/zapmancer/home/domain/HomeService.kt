package com.smach.zapmancer.home.domain

import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.ErrorCode
import com.smach.zapmancer.feature.api.home.ExportActivitiesResponse
import com.smach.zapmancer.feature.api.home.HomeDashboard
import com.smach.zapmancer.feature.api.home.RecentActivity
import com.smach.zapmancer.home.data.HomeRepository
import java.io.File

class HomeService(private val repository: HomeRepository) {

    suspend fun getDashboard(userId: String): DomainResult<HomeDashboard> {
        val context = repository.getUserContext(userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "User not found.")

        val activeCount = repository.getActiveProjectsCount(userId)
        val activities = repository.getRecentActivities(userId)

        val dashboard = HomeDashboard(
            userName = context.name,
            totalEarnings = "$0.00", // Grows as payment milestones are implemented
            earningsGrowth = "+0.0%",
            activeProjectsCount = activeCount,
            totalCapacity = 5,
            systemRating = 0.0,
            recentActivities = activities,
            isClientMode = context.isClientMode,
        )
        return DomainResult.Success(dashboard)
    }

    fun exportActivities(activities: List<RecentActivity>): DomainResult<ExportActivitiesResponse> {
        val timestamp = System.currentTimeMillis()
        val fileName = "activities_$timestamp.csv"
        val dir = File("exports")
        dir.mkdirs()
        val file = File(dir, fileName)

        val csv = buildString {
            appendLine("id,projectName,category,categoryTag,status,date,value")
            activities.forEach { a ->
                appendLine("${a.id},${a.projectName},${a.category},${a.categoryTag},${a.status},${a.date},${a.value}")
            }
        }
        file.writeText(csv)

        return DomainResult.Success(ExportActivitiesResponse(filePath = "exports/$fileName"))
    }
}
