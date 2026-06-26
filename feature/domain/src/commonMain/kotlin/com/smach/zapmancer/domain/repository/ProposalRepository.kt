package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.Proposal

interface ProposalRepository {
    suspend fun submitProposal(proposal: Proposal): Result<Unit, DataError.Network>
    suspend fun getProposalsForProject(projectId: String): Result<List<Proposal>, DataError.Network>
}
