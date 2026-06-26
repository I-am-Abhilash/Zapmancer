package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.UserActivity

interface HomeRepository {
    suspend fun getDashboardData(): Result<HomeDashboard, DataError.Network>
    suspend fun exportActivitiesToCsv(activities: List<UserActivity>): Result<String, DataError.Network>
}
