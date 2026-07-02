package com.smach.zapmancer.proposals.domain

import com.smach.zapmancer.common.CommonResponse
import com.smach.zapmancer.common.DomainResult
import com.smach.zapmancer.proposals.api.Proposal
import com.smach.zapmancer.proposals.api.SubmitProposalRequest
import com.smach.zapmancer.proposals.data.ProposalsRepository

class ProposalsService(private val repository: ProposalsRepository) {

    suspend fun submitProposal(
        freelancerId: String,
        req: SubmitProposalRequest
    ): DomainResult<CommonResponse> {
        repository.submit(freelancerId, req)
        return DomainResult.Success(CommonResponse(success = true, message = "Proposal bid submitted."))
    }

    suspend fun getProposalsForProject(projectId: String): DomainResult<List<Proposal>> =
        DomainResult.Success(repository.getByProject(projectId))
}
