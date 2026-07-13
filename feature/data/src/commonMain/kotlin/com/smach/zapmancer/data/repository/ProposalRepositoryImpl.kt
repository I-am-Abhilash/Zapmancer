package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.dto.CommonResponse
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.common.utils.toUnitResult
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.repository.ProposalRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import com.smach.zapmancer.core.common.dto.Proposal as ProposalDto

class ProposalRepositoryImpl(
    private val client: HttpClient,
) : ProposalRepository {

    override suspend fun submitProposal(proposal: Proposal): Result<Unit, DataError.Network> = safeApiCall<CommonResponse> {
        client.post("proposals") {
            setBody(
                SubmitProposalRequest(
                    projectId = proposal.projectId.ifBlank { "1" },
                    freelancerName = proposal.freelancerName,
                    freelancerRole = proposal.freelancerRole,
                    pitchContent = proposal.pitchContent,
                    budget = proposal.budget,
                    timelineDays = proposal.timelineDays,
                    projectType = proposal.projectType,
                ),
            )
        }
    }.toUnitResult()

    override suspend fun getProposalsForProject(projectId: String): Result<List<Proposal>, DataError.Network> = safeApiCall<List<ProposalDto>> {
        client.get("projects/$projectId/proposals")
    }.let { result ->
        when (result) {
            is Result.Success -> Result.Success(result.data.map { it.toDomain() })
            is Result.Error -> result
        }
    }
}

private fun ProposalDto.toDomain(): Proposal = Proposal(
    id = id.toString(),
    projectId = "",
    freelancerName = freelancerName,
    freelancerRole = freelancerRole,
    pitchContent = pitchContent,
    budget = budget,
    timelineDays = timelineDays,
    projectType = projectType,
)
