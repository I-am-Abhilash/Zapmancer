package com.smach.zapmancer.home.domain

import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.common.ErrorCode
import com.smach.zapmancer.core.common.dto.ExportActivitiesResponse
import com.smach.zapmancer.core.common.dto.HomeDashboard
import com.smach.zapmancer.core.common.dto.RecentActivity
import com.smach.zapmancer.home.data.HomeRepository
import java.io.File

class HomeService(private val repository: HomeRepository) {

    suspend fun getDashboard(userId: String): DomainResult<HomeDashboard> {
        val context = repository.getUserContext(userId)
            ?: return DomainResult.Error(ErrorCode.NOT_FOUND, "User not found.")

        val activeCount = repository.getActiveProjectsCount(userId)
        val capacity = repository.getCapacity(userId)
        val activities = repository.getRecentActivities(userId)

        val dashboard = HomeDashboard(
            userName = context.name,
            // Earnings and rating are populated once payment milestones are implemented.
            totalEarnings = "$0.00",
            earningsGrowth = "+0.0%",
            activeProjectsCount = activeCount,
            totalCapacity = capacity,
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
