package com.smach.zapmancer.domain.model

data class UpdateProfileParams(
    val name: String?,
    val roleTitle: String?,
    val location: String?,
    val about: String?,
    val experience: String?,
    val skills: List<String>?,
    val avatarUrl: String?,
)
