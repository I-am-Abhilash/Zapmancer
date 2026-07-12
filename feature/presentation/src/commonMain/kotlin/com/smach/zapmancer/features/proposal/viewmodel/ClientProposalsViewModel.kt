package com.smach.zapmancer.features.proposal.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.foldTyped
import com.smach.zapmancer.domain.usecase.GetProjectProposalsUseCase
import com.smach.zapmancer.features.proposal.state.ClientProposalsUiState
import kotlinx.coroutines.launch

import com.smach.zapmancer.core.common.utils.toUserMessage

sealed interface ClientProposalsEvent {
    data object Refresh : ClientProposalsEvent
    data class AcceptBid(val freelancerName: String) : ClientProposalsEvent
    data class MessageFreelancer(val freelancerName: String) : ClientProposalsEvent
    data object BackClicked : ClientProposalsEvent
    data class FreelancerClicked(val freelancerId: String) : ClientProposalsEvent
}

sealed interface ClientProposalsEffect {
    data class ShowToast(val message: String) : ClientProposalsEffect
    data object NavigateBack : ClientProposalsEffect
    data class NavigateToFreelancerProfile(val freelancerId: String) : ClientProposalsEffect
}

class ClientProposalsViewModel(
    private val projectId: String,
    private val getProjectProposalsUseCase: GetProjectProposalsUseCase,
) : BaseViewModel<ClientProposalsUiState, ClientProposalsEvent, ClientProposalsEffect>(
    ClientProposalsUiState(),
) {

    init {
        loadProposals()
    }

    override fun onEvent(event: ClientProposalsEvent) {
        when (event) {
            ClientProposalsEvent.Refresh -> loadProposals()

            is ClientProposalsEvent.AcceptBid -> {
                sendEffect(ClientProposalsEffect.ShowToast("Accepted bid from ${event.freelancerName}!"))
            }

            is ClientProposalsEvent.MessageFreelancer -> {
                sendEffect(ClientProposalsEffect.ShowToast("Initiated chat room with ${event.freelancerName}."))
            }

            ClientProposalsEvent.BackClicked -> {
                sendEffect(ClientProposalsEffect.NavigateBack)
            }

            is ClientProposalsEvent.FreelancerClicked -> {
                sendEffect(ClientProposalsEffect.NavigateToFreelancerProfile(event.freelancerId))
            }
        }
    }

    private fun loadProposals() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            getProjectProposalsUseCase(projectId).foldTyped(
                onSuccess = { proposals ->
                    updateState { copy(isLoading = false, proposals = proposals) }
                },
                onError = { error ->
                    updateState { copy(isLoading = false, error = error.toUserMessage()) }
                    sendEffect(ClientProposalsEffect.ShowToast("Failed to load proposals: ${error.toUserMessage()}"))
                },
            )
        }
    }

    companion object {
        private val fallbackProposals = listOf(
            com.smach.zapmancer.domain.model.Proposal(
                freelancerName = "Julian Vancore",
                freelancerRole = "Senior Systems Architect & Interaction Designer",
                pitchContent = "I can architect your high-density canvas rendering engine to process 10,000 updates/second. Over 12 years of systems design experience.",
                budget = "14000",
                timelineDays = "45",
                projectType = "Fixed Price",
            ),
            com.smach.zapmancer.domain.model.Proposal(
                freelancerName = "Sarah Connor",
                freelancerRole = "Infrastructure Security Consultant",
                pitchContent = "Experienced in auditing high-throughput database replication scripts and securing WebSockets connections against malicious attacks.",
                budget = "9500",
                timelineDays = "30",
                projectType = "Fixed Price",
            ),
        )
    }
}
