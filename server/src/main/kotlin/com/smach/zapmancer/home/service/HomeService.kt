package com.smach.zapmancer.home.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.common.dto.ExportActivitiesResponse
import com.smach.zapmancer.core.common.dto.HomeDashboard
import com.smach.zapmancer.core.common.dto.RecentActivity
import com.smach.zapmancer.home.repository.HomeRepository
import org.koin.core.annotation.Single
import java.io.File

@Single
class HomeService(private val repository: HomeRepository) {

    suspend fun getDashboard(userId: String): HomeDashboard {
        val context = repository.getUserContext(userId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "User not found.")

        val activeCount = repository.getActiveProjectsCount(userId)
        val capacity = repository.getCapacity(userId)
        val activities = repository.getRecentActivities(userId)

        return HomeDashboard(
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
    }

    suspend fun exportActivities(activities: List<RecentActivity>): ExportActivitiesResponse = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
        val timestamp = System.currentTimeMillis()
        val fileName = "activities_$timestamp.csv"
        val dir = File("exports")
        dir.mkdirs()
        val file = File(dir, fileName)

        val csv = buildString {
            appendLine("id,projectName,category,categoryTag,status,date,value")
            activities.forEach { a ->
                appendLine(
                    listOf(
                        sanitizeCsv(a.id),
                        sanitizeCsv(a.projectName),
                        sanitizeCsv(a.category),
                        sanitizeCsv(a.categoryTag),
                        sanitizeCsv(a.status),
                        sanitizeCsv(a.date),
                        sanitizeCsv(a.value)
                    ).joinToString(",")
                )
            }
        }
        file.writeText(csv)

        ExportActivitiesResponse(filePath = "exports/$fileName")
    }

    private fun sanitizeCsv(value: String): String {
        var str = value.replace("\"", "\"\"")
        if (str.startsWith("=") || str.startsWith("+") || str.startsWith("-") || str.startsWith("@") || str.startsWith("\t") || str.startsWith("\r")) {
            str = "'$str"
        }
        return "\"$str\""
    }
}


