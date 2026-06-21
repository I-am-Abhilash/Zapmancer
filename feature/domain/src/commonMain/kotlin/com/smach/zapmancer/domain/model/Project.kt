package com.smach.zapmancer.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val membersCount: Int = 0
)


enum class ProjectCategory(val displayName: String) {
    ALL("All"),
    DEVELOPMENT("Development"),
    DESIGN("Design"),
    MARKETING("Marketing");

    companion object {
        fun from(value: String): ProjectCategory =
            entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: ALL
    }
}

enum class ProjectStatus(val displayName: String) {
    ACTIVE("Active"),
    PENDING("Pending"),
    DONE("Done");

    companion object {
        fun from(value: String): ProjectStatus =
            entries.firstOrNull {
                it.name.equals(value, ignoreCase = true)
            } ?: PENDING
    }
}