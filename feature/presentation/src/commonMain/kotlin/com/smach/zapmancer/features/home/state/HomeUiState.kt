package com.smach.zapmancer.features.home.state

import com.smach.zapmancer.domain.model.UserActivity

data class HomeUiState(
    // Freelancer mode stats
    val userName: String = "",
    val totalEarnings: String = "--",
    val earningsGrowth: String = "",
    val activeProjectsCount: Int = 0,
    val totalCapacity: Int = 0,
    val systemRating: Double = 0.0,
    // Client mode stats
    val totalSpent: String = "--",
    val spentGrowth: String = "",
    val activeJobPostsCount: Int = 0,
    val proposalsReceivedCount: Int = 0,
    // Shared
    val recentActivities: List<UserActivity> = emptyList(),
    val isLoading: Boolean = false,
    val isClientMode: Boolean = false,
)
