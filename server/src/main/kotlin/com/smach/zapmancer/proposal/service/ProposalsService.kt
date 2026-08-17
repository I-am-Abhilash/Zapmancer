package com.smach.zapmancer.proposal.service

import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.ErrorCode
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
        val projectOwnerId = repository.getProjectOwnerId(req.projectId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Target project does not exist.")

        if (projectOwnerId == freelancerId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You cannot submit a proposal to your own project.")
        }

        repository.submit(freelancerId, req)
        return CommonResponse(
            success = true,
            message = "Proposal bid submitted.",
        )
    }

    suspend fun getProposalsForProject(userId: String, projectId: String): List<Proposal> {
        val projectOwnerId = repository.getProjectOwnerId(projectId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Project does not exist.")

        if (projectOwnerId != userId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not authorized to view proposals for this project.")
        }

        return repository.getByProject(projectId)
    }
}
