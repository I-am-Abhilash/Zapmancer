package com.smach.zapmancer.data.repository

import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.model.Proposal
import com.smach.zapmancer.domain.repository.ProposalRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody

class ProposalRepositoryImpl(
    private val client: HttpClient,
) : ProposalRepository {

    override suspend fun submitProposal(proposal: Proposal) {
        val result = safeApiCall<CommonResponse> {
            client.post("proposals") {
                setBody(proposal)
            }
        }
        if (result is Result.Error) {
            throw Exception("Failed to submit proposal: ${result.error}")
        }
    }

    override suspend fun getProposalsForProject(projectId: String): Result<List<Proposal>, com.smach.zapmancer.core.common.utils.DataError.Network> = safeApiCall<List<Proposal>> {
        client.get("projects/$projectId/proposals")
    }
}
