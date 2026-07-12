package com.smach.zapmancer.domain.model

data class Project(
    val id: String,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val postedTime: String,
    val membersCount: Int = 0,
)

enum class ProjectStatus(val displayName: String) {
    ACTIVE("Active"),
    PENDING("Pending"),
    COMPLETED("Completed"),
    ;

    companion object {
        fun from(value: String): ProjectStatus = entries.firstOrNull {
            it.name.equals(value, ignoreCase = true)
        } ?: PENDING
    }
}
