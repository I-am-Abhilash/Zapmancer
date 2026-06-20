package com.smach.zapmancer.features.projects.state

import com.smach.zapmancer.features.projects.screen.ProjectPreviewData
import com.smach.zapmancer.features.projects.screen.ProjectUiModel


data class ProjectListUiState(
    val selectedCategory: String = "All",
    val categories: List<String> = listOf(
        "All",
        "Development",
        "Design"
    ),
    val projects: List<ProjectUiModel> = ProjectPreviewData.projects
)
