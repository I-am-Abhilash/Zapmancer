package com.smach.zapmancer.features.projects.state

import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.features.projects.screen.ProjectPreviewData
import com.smach.zapmancer.features.projects.screen.ProjectUiModel


data class ProjectListUiState(
    val category: ProjectCategory = ProjectCategory.ALL,
    val categories: List<ProjectCategory> = ProjectCategory.entries,
    val projects: List<ProjectUiModel> = ProjectPreviewData.projects,
    val isLoading: Boolean = false,
    val error: String? = null
)
