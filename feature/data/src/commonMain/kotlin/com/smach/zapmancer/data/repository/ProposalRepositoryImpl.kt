package com.smach.zapmancer.data.repository

import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.repository.ProposalRepository

class ProposalRepositoryImpl : ProposalRepository {
    override suspend fun submitProposal(proposal: Proposal) {
        // Simulated network/database persistence action
    }
}
