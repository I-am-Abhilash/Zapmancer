package com.smach.zapmancer.features.home.state

import com.smach.zapmancer.domain.model.ActivityStatus

data class HomeUiState(
    val userName: String = "Alex",
    val totalEarnings: String = "$42,850.00",
    val earningsGrowth: String = "+12.4%",
    val activeProjectsCount: Int = 24,
    val totalCapacity: Int = 30,
    val systemRating: Double = 4.98,
    val ratingStars: Int = 5,
    val recentActivities: List<RecentActivity> = defaultActivities,
    val cpuUsage: Float = 0.42f,
    val memoryLoad: Float = 0.18f,
    val lastSyncTime: String = "14:02:11",
    val isLoading: Boolean = false
) {
    companion object {
        val defaultActivities = listOf(
            RecentActivity(
                "1",
                "Neural Engine Optimizer",
                "Infrastructure",
                "AI",
                ActivityStatus.IN_PROGRESS,
                "2h ago",
                "$12,400.00"
            ),
            RecentActivity(
                "2",
                "Dashboard Redesign",
                "Visual Design",
                "UX",
                ActivityStatus.REVIEWING,
                "Yesterday",
                "$4,200.00"
            ),
            RecentActivity(
                "3",
                "SQL Latency Patch",
                "Backend",
                "DB",
                ActivityStatus.COMPLETED,
                "Oct 24",
                "$8,150.00"
            ),
            RecentActivity(
                "4",
                "Security Audit",
                "Compliance",
                "SY",
                ActivityStatus.CRITICAL,
                "Oct 22",
                "$15,000.00"
            )
        )
    }
}

data class RecentActivity(
    val id: String,
    val projectName: String,
    val category: String,
    val categoryTag: String, // AI, UX, DB, SY
    val status: ActivityStatus,
    val date: String,
    val value: String
)
