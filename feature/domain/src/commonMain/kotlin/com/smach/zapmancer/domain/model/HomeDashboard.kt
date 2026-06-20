package com.smach.zapmancer.domain.model

data class HomeDashboard(
    val userName: String,
    val earnings: EarningStats,
    val projectStats: ProjectStats,
    val systemRating: Double,
    val recentActivities: List<UserActivity>
)

data class EarningStats(
    val amount: String,
    val growthPercentage: String
)

data class ProjectStats(
    val activeCount: Int,
    val capacity: Int
)

data class UserActivity(
    val id: String,
    val projectName: String,
    val category: String,
    val tag: String,
    val status: ActivityStatus,
    val timestamp: String,
    val monetaryValue: String
)

enum class ActivityStatus {
    IN_PROGRESS, REVIEWING, COMPLETED, CRITICAL
}
