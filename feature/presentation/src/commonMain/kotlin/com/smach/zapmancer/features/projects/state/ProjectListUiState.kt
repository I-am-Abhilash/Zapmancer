package com.smach.zapmancer.features.projects.state

import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.features.projects.screen.ProjectPreviewData
import com.smach.zapmancer.features.projects.screen.ProjectUiModel

data class ProjectListUiState(
    val category: ProjectCategory = ProjectCategory.ALL,
    val categories: List<ProjectCategory> = ProjectCategory.entries,
    val projects: List<ProjectUiModel> = ProjectPreviewData.projects,
    val isLoading: Boolean = false,
    val error: String? = null,
)

fun Project.toUiModel() = ProjectUiModel(
    id = id,
    category = category,
    status = status,
    title = title,
    description = description,
    progress = progress,
    tags = tags,
    showImagePlaceholder = showImagePlaceholder,
    footerText = footerText,
    membersCount = membersCount,
)
