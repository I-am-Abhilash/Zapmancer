package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import com.smach.zapmancer.features.projects.state.toUiModel
import kotlinx.coroutines.launch

sealed interface ProjectListEvent {
    data object SearchClicked : ProjectListEvent
    data class ProjectClicked(val projectId: Int) : ProjectListEvent
    data object Refresh : ProjectListEvent
}

class ProjectListViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
) : BaseViewModel<ProjectListUiState, ProjectListEvent, Unit>(ProjectListUiState()) {

    init {
        loadProjects()
    }

    override fun onEvent(event: ProjectListEvent) {
        when (event) {
            ProjectListEvent.SearchClicked -> {
            }

            is ProjectListEvent.ProjectClicked -> {
            }

            ProjectListEvent.Refresh -> loadProjects()
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            updateState { copy(projects = emptyList()) }
            getProjectsUseCase().fold(
                onSuccess = { list ->
                    updateState {
                        copy(
                            projects = list.map { it.toUiModel() },
                        )
                    }
                },
                onFailure = { error ->
                    updateState { copy(error = error.message ?: "An unknown error occurred") }
                },
            )
        }
    }
}
