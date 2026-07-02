package com.smach.zapmancer.feature.api.home

import kotlinx.serialization.Serializable

@Serializable
data class HomeDashboard(
    val userName: String,
    val totalEarnings: String,
    val earningsGrowth: String,
    val activeProjectsCount: Int,
    val totalCapacity: Int,
    val systemRating: Double,
    val recentActivities: List<RecentActivity>,
    val isClientMode: Boolean,
)

@Serializable
data class RecentActivity(
    val id: String,
    val projectName: String,
    val category: String,
    val categoryTag: String,
    /** IN_PROGRESS | REVIEWING | COMPLETED | CRITICAL */
    val status: String,
    val date: String,
    val value: String,
)

@Serializable
data class ExportActivitiesRequest(val activities: List<RecentActivity>)

@Serializable
data class ExportActivitiesResponse(val filePath: String)
