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
    data class ProjectClicked(val projectId: Int) : ProjectListEvent
    data object Refresh : ProjectListEvent
    data class CategorySelected(val category: ProjectCategory) : ProjectListEvent
}

class ProjectListViewModel(
    private val getProjectsUseCase: GetProjectsUseCase,
) : BaseViewModel<ProjectListUiState, ProjectListEvent, Unit>(ProjectListUiState()) {

    init {
        loadProjects()
    }

    override fun onEvent(event: ProjectListEvent) {
        when (event) {
            ProjectListEvent.SearchClicked -> Unit
            is ProjectListEvent.ProjectClicked -> Unit
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
