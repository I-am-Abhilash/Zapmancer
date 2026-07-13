package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.repository.ProposalRepository

class GetProjectProposalsUseCase(
    private val repository: ProposalRepository,
) {
    suspend operator fun invoke(projectId: String): Result<List<Proposal>, DataError.Network> = repository.getProposalsForProject(projectId)
}
