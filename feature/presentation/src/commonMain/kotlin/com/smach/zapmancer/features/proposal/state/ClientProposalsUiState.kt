package com.smach.zapmancer.features.proposal.state

import com.smach.zapmancer.domain.model.Proposal

data class ClientProposalsUiState(
    val proposals: List<Proposal> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)
