package com.smach.zapmancer.features.home.state

import com.smach.zapmancer.domain.model.UserActivity

data class HomeUiState(
    val userName: String = "Alex",
    val totalEarnings: String = "$42,850.00",
    val earningsGrowth: String = "+12.4%",
    val activeProjectsCount: Int = 24,
    val totalCapacity: Int = 30,
    val systemRating: Double = 4.98,
    val ratingStars: Int = 5,
    val recentActivities: List<UserActivity> = emptyList(),
    val cpuUsage: Float = 0.42f,
    val memoryLoad: Float = 0.18f,
    val lastSyncTime: String = "14:02:11",
    val isLoading: Boolean = false,
    val isClientMode: Boolean = false
)
