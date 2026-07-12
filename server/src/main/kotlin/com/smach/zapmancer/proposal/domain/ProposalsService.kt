package com.smach.zapmancer.proposal.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.proposal.data.ProposalsRepository

class ProposalsService(private val repository: ProposalsRepository) {

    suspend fun submitProposal(
        freelancerId: String,
        req: SubmitProposalRequest,
    ): DomainResult<CommonResponse> {
        repository.submit(freelancerId, req)
        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "Proposal bid submitted."
            )
        )
    }

    suspend fun getProposalsForProject(projectId: String): DomainResult<List<Proposal>> =
        DomainResult.Success(repository.getByProject(projectId))
}
