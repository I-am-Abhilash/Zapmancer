package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.domain.usecase.ApplyProjectUseCase
import com.smach.zapmancer.domain.usecase.GetProjectDetailUseCase
import com.smach.zapmancer.domain.usecase.SaveProjectUseCase
import com.smach.zapmancer.features.projects.state.ProjectDetailUiState
import kotlinx.coroutines.launch

sealed interface ProjectDetailEvent {
    data object LoadProject : ProjectDetailEvent
    data object ToggleSave : ProjectDetailEvent
    data object Apply : ProjectDetailEvent
}

sealed interface ProjectDetailEffect {
    data class ShowToast(val message: String) : ProjectDetailEffect
}

class ProjectDetailViewModel(
    private val projectId: String,
    private val getProjectDetailUseCase: GetProjectDetailUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val applyProjectUseCase: ApplyProjectUseCase
) : BaseViewModel<ProjectDetailUiState, ProjectDetailEvent, ProjectDetailEffect>(ProjectDetailUiState()) {

    init {
        loadProject()
    }

    override fun onEvent(event: ProjectDetailEvent) {
        when (event) {
            ProjectDetailEvent.LoadProject -> loadProject()
            ProjectDetailEvent.ToggleSave -> toggleSave()
            ProjectDetailEvent.Apply -> apply()
        }
    }

    private fun loadProject() {
        viewModelScope.launch {
            updateState { copy(isLoading = true) }
            getProjectDetailUseCase(projectId).fold(
                onSuccess = { detail ->
                    updateState {
                        copy(
                            id = detail.id,
                            category = detail.category,
                            title = detail.title,
                            postedTime = detail.postedTime,
                            location = detail.location,
                            isPaymentVerified = detail.isPaymentVerified,
                            projectScope = detail.projectScope,
                            deliverables = detail.deliverables,
                            skills = detail.skills,
                            budgetRange = detail.budgetRange,
                            projectType = detail.projectType,
                            timeline = detail.timeline,
                            estStart = detail.estStart,
                            clientName = detail.clientName,
                            clientIndustry = detail.clientIndustry,
                            clientLocation = detail.clientLocation,
                            clientProjectsCount = detail.clientProjectsCount,
                            clientRating = detail.clientRating,
                            isSaved = detail.isSaved,
                            isClientActive = detail.isClientActive,
                            isIdentityVerified = detail.isIdentityVerified,
                            isPhoneVerified = detail.isPhoneVerified,
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    updateState { copy(isLoading = false) }
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to load project details: ${error.message}"))
                }
            )
        }
    }

    private fun toggleSave() {
        val nextSavedState = !uiState.value.isSaved
        viewModelScope.launch {
            saveProjectUseCase(projectId, nextSavedState).fold(
                onSuccess = {
                    updateState { copy(isSaved = nextSavedState) }
                    sendEffect(ProjectDetailEffect.ShowToast(if (nextSavedState) "Project Saved" else "Project Unsaved"))
                },
                onFailure = { error ->
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to update save state: ${error.message}"))
                }
            )
        }
    }

    private fun apply() {
        viewModelScope.launch {
            applyProjectUseCase(projectId).fold(
                onSuccess = {
                    sendEffect(ProjectDetailEffect.ShowToast("Applied successfully!"))
                },
                onFailure = { error ->
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to apply: ${error.message}"))
                }
            )
        }
    }
}
