package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.domain.usecase.GetProjectsUseCase
import com.smach.zapmancer.features.projects.screen.ProjectUiModel
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import kotlinx.coroutines.launch

sealed interface ProjectListEvent {
    data class CategorySelected(val category: String) : ProjectListEvent
    data object SearchClicked : ProjectListEvent
    data class ProjectClicked(val projectId: Int) : ProjectListEvent
    data object Refresh : ProjectListEvent
}

class ProjectListViewModel(
    private val getProjectsUseCase: GetProjectsUseCase
) : BaseViewModel<ProjectListUiState, ProjectListEvent, Unit>(ProjectListUiState()) {

    init {
        loadProjects()
    }

    override fun onEvent(event: ProjectListEvent) {
        when (event) {
            is ProjectListEvent.CategorySelected -> {
                updateState { copy(selectedCategory = event.category) }
            }

            ProjectListEvent.SearchClicked -> {
                // Implement search action if needed
            }

            is ProjectListEvent.ProjectClicked -> {
                // Implement project selection logic if needed
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
                            projects = list.map { domainProject ->
                                ProjectUiModel(
                                    id = domainProject.id,
                                    category = ProjectCategory.from(domainProject.category),
                                    status = ProjectStatus.from(domainProject.status),
                                    title = domainProject.title,
                                    description = domainProject.description,
                                    progress = domainProject.progress,
                                    tags = domainProject.tags,
                                    showImagePlaceholder = domainProject.showImagePlaceholder,
                                    footerText = domainProject.footerText,
                                    membersCount = domainProject.membersCount
                                )
                            }
                        )
                    }
                },
                onFailure = { error ->
                    updateState { copy(error = error.message ?: "An unknown error occurred") }
                }
            )
        }
    }
}