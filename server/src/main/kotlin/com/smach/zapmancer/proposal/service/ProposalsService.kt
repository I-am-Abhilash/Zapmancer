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
        if (req.projectId.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Project ID cannot be blank.")
        }
        if (req.pitchContent.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Pitch content cannot be blank.")
        }
        if (req.budget.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Budget cannot be blank.")
        }
        if (req.timelineDays.isBlank()) {
            throw ApiException(ErrorCode.BAD_REQUEST, "Timeline days cannot be blank.")
        }

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

    suspend fun getMyProposals(freelancerId: String): List<Proposal> = repository.getByFreelancer(freelancerId)

    suspend fun acceptProposal(clientId: String, proposalId: Int): CommonResponse {
        val proposal = repository.getProposalById(proposalId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Proposal not found.")

        val projectOwnerId = repository.getProjectOwnerId(proposal.projectId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Target project does not exist.")

        if (projectOwnerId != clientId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not authorized to accept proposals for this project.")
        }

        val contractFormed = repository.acceptProposalAndFormContract(
            proposalId = proposalId,
            clientId = clientId,
            freelancerId = proposal.freelancerId,
            projectId = proposal.projectId,
        )

        if (!contractFormed) {
            throw ApiException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to form contract for proposal.")
        }

        return CommonResponse(
            success = true,
            message = "Proposal accepted and contract formed successfully.",
        )
    }

    suspend fun rejectProposal(clientId: String, proposalId: Int): CommonResponse {
        val proposal = repository.getProposalById(proposalId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Proposal not found.")

        val projectOwnerId = repository.getProjectOwnerId(proposal.projectId)
            ?: throw ApiException(ErrorCode.NOT_FOUND, "Target project does not exist.")

        if (projectOwnerId != clientId) {
            throw ApiException(ErrorCode.FORBIDDEN, "You are not authorized to reject proposals for this project.")
        }

        repository.updateStatus(proposalId, "REJECTED")
        return CommonResponse(
            success = true,
            message = "Proposal rejected.",
        )
    }

    suspend fun withdrawProposal(freelancerId: String, proposalId: Int): CommonResponse {
        val withdrawn = repository.withdraw(proposalId, freelancerId)
        if (!withdrawn) {
            throw ApiException(ErrorCode.FORBIDDEN, "Proposal not found or you are not authorized to withdraw it.")
        }
        return CommonResponse(
            success = true,
            message = "Proposal withdrawn successfully.",
        )
    }
}
