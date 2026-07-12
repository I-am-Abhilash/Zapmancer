package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.usecase.ApplyProjectUseCase
import com.smach.zapmancer.domain.usecase.GetProjectDetailUseCase
import com.smach.zapmancer.domain.usecase.SaveProjectUseCase
import com.smach.zapmancer.features.projects.state.ProjectDetailUiState
import kotlinx.coroutines.launch

sealed interface ProjectDetailEvent {
    data object LoadProject : ProjectDetailEvent
    data object ToggleSave : ProjectDetailEvent
    data object Apply : ProjectDetailEvent
    data object BackClicked : ProjectDetailEvent
}

sealed interface ProjectDetailEffect {
    data class ShowToast(val message: String) : ProjectDetailEffect
    data object NavigateBack : ProjectDetailEffect
}

class ProjectDetailViewModel(
    private val projectId: String,
    private val getProjectDetailUseCase: GetProjectDetailUseCase,
    private val saveProjectUseCase: SaveProjectUseCase,
    private val applyProjectUseCase: ApplyProjectUseCase,
) : BaseViewModel<ProjectDetailUiState, ProjectDetailEvent, ProjectDetailEffect>(
    ProjectDetailUiState(),
) {

    init {
        loadProject()
    }

    override fun onEvent(event: ProjectDetailEvent) {
        when (event) {
            ProjectDetailEvent.LoadProject -> loadProject()
            ProjectDetailEvent.ToggleSave -> toggleSave()
            ProjectDetailEvent.Apply -> apply()
            ProjectDetailEvent.BackClicked -> sendEffect(ProjectDetailEffect.NavigateBack)
        }
    }

    private fun loadProject() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getProjectDetailUseCase(projectId).foldTyped(
                onSuccess = { detail ->
                    updateState {
                        copy(
                            id = detail.id,
                            category = detail.category,
                            title = detail.title,
                            postedTime = detail.postedTime,
                            location = detail.location,
                            isPaymentVerified = detail.isPaymentVerified,
                            projectScope = detail.projectScope.orEmpty(),
                            deliverables = detail.deliverables,
                            skills = detail.skills,
                            budgetRange = detail.budgetRange,
                            projectType = detail.projectType,
                            timeline = detail.timeline.orEmpty(),
                            estStart = detail.estStart.orEmpty(),
                            clientName = detail.clientName,
                            clientIndustry = detail.clientIndustry,
                            clientLocation = detail.clientLocation,
                            clientProjectsCount = detail.clientProjectsCount,
                            clientRating = detail.clientRating,
                            isSaved = detail.isSaved,
                            isClientActive = detail.isClientActive,
                            isIdentityVerified = detail.isIdentityVerified,
                            isPhoneVerified = detail.isPhoneVerified,
                            isLoading = false,
                            error = null,
                        )
                    }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to load project details: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun toggleSave() {
        val nextSavedState = !uiState.value.isSaved
        viewModelScope.launch {
            saveProjectUseCase(projectId, nextSavedState).foldTyped(
                onSuccess = {
                    updateState { copy(isSaved = nextSavedState) }
                    sendEffect(ProjectDetailEffect.ShowToast(if (nextSavedState) "Project Saved" else "Project Unsaved"))
                },
                onError = { error ->
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to update save state: ${error.toUserMessage()}"))
                },
            )
        }
    }

    private fun apply() {
        viewModelScope.launch {
            applyProjectUseCase(projectId).foldTyped(
                onSuccess = {
                    sendEffect(ProjectDetailEffect.ShowToast("Applied successfully!"))
                },
                onError = { error ->
                    sendEffect(ProjectDetailEffect.ShowToast("Failed to apply: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
