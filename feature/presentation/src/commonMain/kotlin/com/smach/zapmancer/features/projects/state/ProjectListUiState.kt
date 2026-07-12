package com.smach.zapmancer.features.projects.state

import androidx.compose.ui.graphics.vector.ImageVector
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus

data class ProjectUiModel(
    val id: String,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val footerIcon: ImageVector? = null,
    val membersCount: Int = 0,
)

data class ProjectListUiState(
    val category: ProjectCategory = ProjectCategory.ALL,
    val categories: List<ProjectCategory> = ProjectCategory.entries,
    val projects: List<ProjectUiModel> = emptyList(),
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
    showImagePlaceholder = category == ProjectCategory.DESIGN,
    footerText = postedTime,
    membersCount = membersCount,
)
