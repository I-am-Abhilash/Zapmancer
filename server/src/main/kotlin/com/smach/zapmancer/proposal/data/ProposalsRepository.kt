package com.smach.zapmancer.proposal.data

import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.ProposalsTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import kotlin.time.Clock.System

class ProposalsRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    suspend fun submit(freelancerId: String, req: SubmitProposalRequest): Int = dbQuery {
        ProposalsTable.insert {
            it[ProposalsTable.projectId] = req.projectId
            it[ProposalsTable.freelancerId] = freelancerId
            it[ProposalsTable.freelancerName] = req.freelancerName
            it[ProposalsTable.freelancerRole] = req.freelancerRole
            it[ProposalsTable.pitchContent] = req.pitchContent
            it[ProposalsTable.budget] = req.budget
            it[ProposalsTable.timelineDays] = req.timelineDays
            it[ProposalsTable.projectType] = req.projectType
            it[ProposalsTable.createdAt] = now()
        }[ProposalsTable.id]
    }

    suspend fun getByProject(projectId: String): List<Proposal> = dbQuery {
        ProposalsTable.selectAll()
            .where { ProposalsTable.projectId eq projectId }
            .map { row ->
                Proposal(
                    id = row[ProposalsTable.id],
                    freelancerName = row[ProposalsTable.freelancerName],
                    freelancerRole = row[ProposalsTable.freelancerRole],
                    pitchContent = row[ProposalsTable.pitchContent],
                    budget = row[ProposalsTable.budget],
                    timelineDays = row[ProposalsTable.timelineDays],
                    projectType = row[ProposalsTable.projectType],
                )
            }
    }
}
