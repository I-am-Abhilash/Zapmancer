package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.repository.ProposalRepository
import org.koin.core.annotation.Factory

@Factory
class AcceptProposalUseCase(
    private val repository: ProposalRepository,
) {
    suspend operator fun invoke(id: String): Result<Unit, DataError.Network> = repository.acceptProposal(id)
}
