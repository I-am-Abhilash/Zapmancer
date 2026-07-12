package com.smach.zapmancer.domain.model

data class UserActivity(
    val id: String,
    val projectName: String,
    val category: ProjectCategory,
    val tag: String,
    val status: ActivityStatus,
    val timestamp: Long,
    val monetaryValue: String,
)
