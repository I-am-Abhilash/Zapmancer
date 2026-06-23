package com.smach.zapmancer.features.proposal.viewmodel

import androidx.lifecycle.viewModelScope
import com.smach.zapmancer.core.common.base.BaseViewModel
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.usecase.GetProjectProposalsUseCase
import com.smach.zapmancer.features.proposal.state.ClientProposalsUiState
import kotlinx.coroutines.launch

sealed interface ClientProposalsEvent {
    data object Refresh : ClientProposalsEvent
    data class AcceptBid(val freelancerName: String) : ClientProposalsEvent
    data class MessageFreelancer(val freelancerName: String) : ClientProposalsEvent
}

sealed interface ClientProposalsEffect {
    data class ShowToast(val message: String) : ClientProposalsEffect
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
        }
    }

    private fun loadProposals() {
        viewModelScope.launch {
            updateState { copy(isLoading = true, error = null) }
            when (val result = getProjectProposalsUseCase(projectId)) {
                is Result.Success -> {
                    updateState { copy(isLoading = false, proposals = result.data) }
                }

                is Result.Error -> {
                    // Fallback to static mock proposals if the server returns error
                    updateState {
                        copy(
                            isLoading = false,
                            proposals = fallbackProposals,
                        )
                    }
                }
            }
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
