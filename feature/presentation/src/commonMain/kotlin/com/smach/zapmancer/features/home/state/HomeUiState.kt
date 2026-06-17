package com.smach.zapmancer.features.home.state

data class HomeUiState(
    val userName: String = "Alex",
    val totalEarnings: String = "$42,850.00",
    val earningsGrowth: String = "+12.4%",
    val activeProjectsCount: Int = 24,
    val totalCapacity: Int = 30,
    val systemRating: Double = 4.98,
    val ratingStars: Int = 5,
    val recentActivities: List<RecentActivity> = emptyList(),
    val cpuUsage: Float = 0.42f,
    val memoryLoad: Float = 0.18f,
    val lastSyncTime: String = "14:02:11",
    val isLoading: Boolean = false
)

data class RecentActivity(
    val id: String,
    val projectName: String,
    val category: String,
    val categoryTag: String, // AI, UX, DB, SY
    val status: ActivityStatus,
    val date: String,
    val value: String
)

enum class ActivityStatus {
    IN_PROGRESS, REVIEWING, COMPLETED, CRITICAL
}
