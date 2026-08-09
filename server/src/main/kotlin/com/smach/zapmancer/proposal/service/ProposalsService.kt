package com.smach.zapmancer.proposal.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.proposal.repository.ProposalsRepository
import org.koin.core.annotation.Single

@Single
class ProposalsService(private val repository: ProposalsRepository) {

    suspend fun submitProposal(
        freelancerId: String,
        req: SubmitProposalRequest,
    ): CommonResponse {
        repository.submit(freelancerId, req)
        return CommonResponse(
            success = true,
            message = "Proposal bid submitted.",
        )
    }

    suspend fun getProposalsForProject(projectId: String): List<Proposal> = repository.getByProject(projectId)
}

