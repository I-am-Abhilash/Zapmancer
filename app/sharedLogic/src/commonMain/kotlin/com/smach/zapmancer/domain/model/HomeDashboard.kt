package com.smach.zapmancer.domain.model

data class HomeDashboard(
    val userName: String,
    val earnings: EarningStats,
    val projectStats: ProjectStats,
    val systemRating: Double,
    val recentActivities: List<UserActivity>,
    val clientStats: ClientDashboardStats? = null,
)
