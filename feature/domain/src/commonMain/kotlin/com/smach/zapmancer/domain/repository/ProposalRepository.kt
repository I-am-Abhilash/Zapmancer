package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.domain.model.Proposal

interface ProposalRepository {
    suspend fun submitProposal(proposal: Proposal)
    suspend fun getProposalsForProject(projectId: String): com.smach.zapmancer.core.common.utils.Result<List<Proposal>, com.smach.zapmancer.core.common.utils.DataError.Network>
}
