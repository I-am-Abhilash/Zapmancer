package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.UserActivity

interface HomeRepository {
    suspend fun getDashboardData(): HomeDashboard
    suspend fun exportActivitiesToCsv(activities: List<UserActivity>): String
}
