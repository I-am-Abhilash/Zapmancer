package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.domain.model.Proposal

interface ProposalRepository {
    suspend fun submitProposal(proposal: Proposal)
}
