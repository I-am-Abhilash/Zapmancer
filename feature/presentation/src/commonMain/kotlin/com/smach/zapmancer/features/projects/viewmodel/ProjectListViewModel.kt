package com.smach.zapmancer.features.projects.viewmodel

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Devices
import androidx.lifecycle.ViewModel
import com.smach.zapmancer.features.common.theme.ZapTeal
import com.smach.zapmancer.features.projects.screen.ProjectUiModel
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed interface ProjectListEvent {
    data class CategorySelected(
        val category: String
    ) : ProjectListEvent

    data object SearchClicked : ProjectListEvent

    data class ProjectClicked(
        val projectId: Int
    ) : ProjectListEvent
}
object ProjectPreviewData {
    val state =
        ProjectListUiState(
            projects = listOf(
                ProjectUiModel(
                    id = 1,
                    category = "Development",
                    status = "ACTIVE",
                    title = "Neural Engine Alpha",
                    description = "...",
                    icon = Icons.Outlined.Devices,
                    accentColor = ZapTeal,
                    progress = 78,
                    footerText = "Optimization Progress",
                    membersCount = 2
                ),
        )
    )
}

class ProjectListViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ProjectListUiState(
                projects = ProjectPreviewData.state.projects
            )
        )

    val uiState = _uiState.asStateFlow()

    fun onEvent(
        event: ProjectListEvent
    ) {
        when (event) {

            is ProjectListEvent.CategorySelected -> {
                _uiState.update {
                    it.copy(
                        selectedCategory = event.category
                    )
                }
            }

            ProjectListEvent.SearchClicked -> {
                // TODO: Implement search navigation or state update
            }

            is ProjectListEvent.ProjectClicked -> {
                // TODO: Handle project selection, e.g., navigate to details
            }
        }
    }
}