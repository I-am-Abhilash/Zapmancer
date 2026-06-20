package com.smach.zapmancer.data.repository

import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.domain.model.EarningStats
import com.smach.zapmancer.domain.model.HomeDashboard
import com.smach.zapmancer.domain.model.ProjectStats
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.domain.repository.HomeRepository

class HomeRepositoryImpl : HomeRepository {
    override suspend fun getDashboardData(): HomeDashboard {
        return HomeDashboard(
            userName = "Alex",
            earnings = EarningStats(
                amount = "$42,850.00",
                growthPercentage = "+12.4%"
            ),
            projectStats = ProjectStats(
                activeCount = 24,
                capacity = 30
            ),
            systemRating = 4.98,
            recentActivities = listOf(
                UserActivity(
                    id = "1",
                    projectName = "Neural Engine Optimizer",
                    category = "Infrastructure",
                    tag = "AI",
                    status = ActivityStatus.IN_PROGRESS,
                    timestamp = "2h ago",
                    monetaryValue = "$12,400.00"
                ),
                UserActivity(
                    id = "2",
                    projectName = "Dashboard Redesign",
                    category = "Visual Design",
                    tag = "UX",
                    status = ActivityStatus.REVIEWING,
                    timestamp = "Yesterday",
                    monetaryValue = "$4,200.00"
                ),
                UserActivity(
                    id = "3",
                    projectName = "SQL Latency Patch",
                    category = "Backend",
                    tag = "DB",
                    status = ActivityStatus.COMPLETED,
                    timestamp = "Oct 24",
                    monetaryValue = "$8,150.00"
                ),
                UserActivity(
                    id = "4",
                    projectName = "Security Audit",
                    category = "Compliance",
                    tag = "SY",
                    status = ActivityStatus.CRITICAL,
                    timestamp = "Oct 22",
                    monetaryValue = "$15,000.00"
                )
            )
        )
    }

    override suspend fun exportActivitiesToCsv(activities: List<UserActivity>): String {
        // Return a mock CSV path
        return "activities_export.csv"
    }
}
