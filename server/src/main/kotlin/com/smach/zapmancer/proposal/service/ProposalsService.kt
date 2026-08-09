package com.smach.zapmancer.proposal.service

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.DomainResult
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.proposal.repository.ProposalsRepository
import org.koin.core.annotation.Single
import org.koin.core.annotation.Singleton

@Single
class ProposalsService(private val repository: ProposalsRepository) {

    suspend fun submitProposal(
        freelancerId: String,
        req: SubmitProposalRequest,
    ): DomainResult<CommonResponse> {
        repository.submit(freelancerId, req)
        return DomainResult.Success(
            CommonResponse(
                success = true,
                message = "Proposal bid submitted.",
            ),
        )
    }

    suspend fun getProposalsForProject(projectId: String): DomainResult<List<Proposal>> = DomainResult.Success(repository.getByProject(projectId))
}
