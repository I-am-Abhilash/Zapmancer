package com.smach.zapmancer.data.repository

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

class ProposalRepositoryImpl(
    private val client: HttpClient,
) : ProposalRepository {

    override suspend fun submitProposal(proposal: Proposal): Result<Unit, DataError.Network> =
        safeApiCall<CommonResponse> {
            client.post("proposals") {
                setBody(proposal)
            }
        }.toUnitResult()

    override suspend fun getProposalsForProject(projectId: String): Result<List<Proposal>, DataError.Network> =
        safeApiCall<List<Proposal>> { client.get("projects/$projectId/proposals") }
}