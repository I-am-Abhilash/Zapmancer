package com.smach.zapmancer.presentation.proposal.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.core.common.utils.toUserMessage
import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.usecase.SubmitProposalUseCase
import com.smach.zapmancer.presentation.proposal.state.ProposalUiState
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

sealed interface ProposalEvent {
    data class OnPitchChanged(val pitch: String) : ProposalEvent
    data class OnBudgetChanged(val budget: String) : ProposalEvent
    data class OnTimelineChanged(val timeline: String) : ProposalEvent
    data class StepChanged(val step: Int) : ProposalEvent
    data object Submit : ProposalEvent
    data object BackClicked : ProposalEvent
    data object DashboardClicked : ProposalEvent
}

sealed interface ProposalEffect {
    data class ShowToast(val message: String) : ProposalEffect
    data object NavigateBack : ProposalEffect
    data object NavigateHome : ProposalEffect
}

@KoinViewModel
class ProposalViewModel(
    private val submitProposalUseCase: SubmitProposalUseCase,
) : BaseViewModel<ProposalUiState, ProposalEvent, ProposalEffect>(ProposalUiState()) {

    override fun onEvent(event: ProposalEvent) {
        when (event) {
            is ProposalEvent.OnPitchChanged -> updateState { copy(pitchContent = event.pitch) }
            is ProposalEvent.OnBudgetChanged -> updateState { copy(budget = event.budget) }
            is ProposalEvent.OnTimelineChanged -> updateState { copy(timelineDays = event.timeline) }
            is ProposalEvent.StepChanged -> updateState { copy(currentStep = event.step) }
            ProposalEvent.Submit -> submitProposal()
            ProposalEvent.BackClicked -> sendEffect(ProposalEffect.NavigateBack)
            ProposalEvent.DashboardClicked -> sendEffect(ProposalEffect.NavigateHome)
        }
    }

    private fun submitProposal() {
        val current = uiState.value
        viewModelScope.launch {
            updateState { copy(isSubmitting = true, error = null) }

            val domainProposal = Proposal(
                id = "",
                projectId = "",
                freelancerName = current.freelancerName,
                freelancerRole = current.freelancerRole,
                pitchContent = current.pitchContent,
                budget = current.budget,
                timelineDays = current.timelineDays,
                projectType = current.projectType,
            )

            submitProposalUseCase(domainProposal).foldTyped(
                onSuccess = {
                    updateState { copy(isSubmitting = false, isSubmitted = true) }
                    sendEffect(ProposalEffect.ShowToast("Proposal submitted successfully!"))
                },
                onError = { error ->
                    updateState { copy(isSubmitting = false, error = error.toUserMessage()) }
                    sendEffect(ProposalEffect.ShowToast("Submission failed: ${error.toUserMessage()}"))
                },
            )
        }
    }
}
