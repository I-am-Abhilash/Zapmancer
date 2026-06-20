package com.smach.zapmancer.domain.model

data class Project(
    val id: Int,
    val category: String,
    val status: String,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val membersCount: Int = 0
)
