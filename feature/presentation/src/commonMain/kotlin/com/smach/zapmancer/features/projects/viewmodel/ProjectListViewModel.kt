package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import com.smach.zapmancer.features.projects.state.toUiModel
import kotlinx.coroutines.launch

sealed interface ProjectListEvent {
    data object SearchClicked : ProjectListEvent
    data class ProjectClicked(val projectId: String) : ProjectListEvent
    data object Refresh : ProjectListEvent
    data class CategorySelected(val category: ProjectCategory) : ProjectListEvent
}

sealed interface ProjectListEffect {
    data class NavigateToProjectDetail(val projectId: String) : ProjectListEffect
    data object NavigateToSearch : ProjectListEffect
}

class ProjectListViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
) : BaseViewModel<ProjectListUiState, ProjectListEvent, ProjectListEffect>(ProjectListUiState()) {

    init {
        loadProjects()
    }

    override fun onEvent(event: ProjectListEvent) {
        when (event) {
            ProjectListEvent.SearchClicked -> sendEffect(ProjectListEffect.NavigateToSearch)
            is ProjectListEvent.ProjectClicked -> sendEffect(
                ProjectListEffect.NavigateToProjectDetail(
                    event.projectId
                )
            )

            ProjectListEvent.Refresh -> loadProjects()
            is ProjectListEvent.CategorySelected -> updateState { copy(category = event.category) }
        }
    }

    private fun loadProjects() {
        viewModelScope.launch {
            updateState { copy(projects = emptyList(), isLoading = true) }
            getProjectsUseCase().foldTyped(
                onSuccess = { list ->
                    updateState {
                        copy(
                            projects = list.map { it.toUiModel() },
                            isLoading = false,
                            error = null,
                        )
                    }
                },
                onError = { error ->
                    updateState {
                        copy(
                            isLoading = false,
                            error = error.toUserMessage(),
                        )
                    }
                },
            )
        }
    }
}
