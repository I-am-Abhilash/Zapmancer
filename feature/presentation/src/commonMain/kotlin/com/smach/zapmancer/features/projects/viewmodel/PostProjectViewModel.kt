package com.smach.zapmancer.features.projects.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.ProjectDetail
import com.smach.zapmancer.domain.usecase.PostProjectUseCase
import com.smach.zapmancer.features.projects.state.PostProjectUiState
import kotlinx.coroutines.launch

sealed interface PostProjectEvent {
    data class OnTitleChanged(val title: String) : PostProjectEvent
    data class OnCategoryChanged(val category: String) : PostProjectEvent
    data class OnDescriptionChanged(val description: String) : PostProjectEvent
    data class OnBudgetChanged(val budget: String) : PostProjectEvent
    data class OnTimelineChanged(val timeline: String) : PostProjectEvent
    data class OnDeliverableInputChanged(val input: String) : PostProjectEvent
    data object AddDeliverable : PostProjectEvent
    data class RemoveDeliverable(val index: Int) : PostProjectEvent
    data class OnSkillInputChanged(val input: String) : PostProjectEvent
    data object AddSkill : PostProjectEvent
    data class RemoveSkill(val index: Int) : PostProjectEvent
    data object Submit : PostProjectEvent
}

sealed interface PostProjectEffect {
    data class ShowToast(val message: String) : PostProjectEffect
}

class PostProjectViewModel(
    private val postProjectUseCase: PostProjectUseCase,
) : BaseViewModel<PostProjectUiState, PostProjectEvent, PostProjectEffect>(PostProjectUiState()) {

    override fun onEvent(event: PostProjectEvent) {
        when (event) {
            is PostProjectEvent.OnTitleChanged -> updateState { copy(title = event.title) }

            is PostProjectEvent.OnCategoryChanged -> updateState { copy(category = event.category) }

            is PostProjectEvent.OnDescriptionChanged -> updateState { copy(description = event.description) }

            is PostProjectEvent.OnBudgetChanged -> updateState { copy(budgetRange = event.budget) }

            is PostProjectEvent.OnTimelineChanged -> updateState { copy(timeline = event.timeline) }

            is PostProjectEvent.OnDeliverableInputChanged -> updateState {
                copy(
                    currentDeliverableInput = event.input,
                )
            }

            PostProjectEvent.AddDeliverable -> {
                val input = uiState.value.currentDeliverableInput.trim()
                if (input.isNotEmpty()) {
                    updateState {
                        copy(
                            deliverables = deliverables + input,
                            currentDeliverableInput = "",
                        )
                    }
                }
            }

            is PostProjectEvent.RemoveDeliverable -> {
                updateState { copy(deliverables = deliverables.filterIndexed { idx, _ -> idx != event.index }) }
            }

            is PostProjectEvent.OnSkillInputChanged -> updateState { copy(currentSkillInput = event.input) }

            PostProjectEvent.AddSkill -> {
                val input = uiState.value.currentSkillInput.trim()
                if (input.isNotEmpty()) {
                    updateState { copy(skills = skills + input, currentSkillInput = "") }
                }
            }

            is PostProjectEvent.RemoveSkill -> {
                updateState { copy(skills = skills.filterIndexed { idx, _ -> idx != event.index }) }
            }

            PostProjectEvent.Submit -> submitProject()
        }
    }

    private fun submitProject() {
        val state = uiState.value
        if (state.title.isBlank() || state.description.isBlank()) {
            sendEffect(PostProjectEffect.ShowToast("Title and Description are required!"))
            return
        }
        viewModelScope.launch {
            updateState { copy(isSubmitting = true, error = null) }
            val project = ProjectDetail(
                id = "",
                category = state.category,
                title = state.title,
                postedTime = "Just now",
                location = "Remote",
                isPaymentVerified = true,
                projectScope = state.description,
                deliverables = state.deliverables,
                skills = state.skills,
                budgetRange = state.budgetRange,
                projectType = "Fixed Price",
                timeline = state.timeline,
                estStart = "Immediate",
                clientName = "You",
                clientIndustry = "Tech",
                clientLocation = "Remote",
                clientProjectsCount = 1,
                clientRating = 5.0,
                isSaved = false,
                isClientActive = true,
                isIdentityVerified = true,
                isPhoneVerified = true,
            )
            when (val result = postProjectUseCase(project)) {
                is Result.Success -> {
                    updateState { copy(isSubmitting = false, isSubmitted = true) }
                    sendEffect(PostProjectEffect.ShowToast("Project posted successfully!"))
                }

                is Result.Error -> {
                    updateState { copy(isSubmitting = false, error = "Failed to post project") }
                    sendEffect(PostProjectEffect.ShowToast("Failed to post project: ${result.error}"))
                }
            }
        }
    }
}
