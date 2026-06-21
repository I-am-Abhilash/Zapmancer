package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class HomeDashboard(
    val userName: String,
    val earnings: EarningStats,
    val projectStats: ProjectStats,
    val systemRating: Double,
    val recentActivities: List<UserActivity>
)

@Serializable
data class EarningStats(
    val amount: String,
    val growthPercentage: String
)

@Serializable
data class ProjectStats(
    val activeCount: Int,
    val capacity: Int
)

@Serializable
data class UserActivity(
    val id: String,
    val projectName: String,
    val category: String,
    val tag: String,
    val status: ActivityStatus,
    val timestamp: String,
    val monetaryValue: String
)

@Serializable
enum class ActivityStatus {
    IN_PROGRESS, REVIEWING, COMPLETED, CRITICAL
}
