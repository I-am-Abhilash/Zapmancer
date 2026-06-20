package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.repository.ProposalRepository

class SubmitProposalUseCase(
    private val repository: ProposalRepository
) {
    suspend operator fun invoke(proposal: Proposal): Result<Unit> {
        return try {
            repository.submitProposal(proposal)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
