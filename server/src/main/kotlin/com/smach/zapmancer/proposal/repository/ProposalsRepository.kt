package com.smach.zapmancer.proposal.repository

import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.core.database.ConversationsTable
import com.smach.zapmancer.core.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.core.database.NotificationActionsTable
import com.smach.zapmancer.core.database.NotificationsTable
import com.smach.zapmancer.core.database.ProjectsTable
import com.smach.zapmancer.core.database.ProposalsTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.or
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import java.util.UUID
import kotlin.time.Clock.System

data class ProposalRecord(
    val id: Int,
    val projectId: String,
    val freelancerId: String,
    val freelancerName: String,
    val freelancerRole: String,
    val pitchContent: String,
    val budget: String,
    val timelineDays: String,
    val projectType: String,
    val status: String,
    val createdAt: String,
)

class ProposalsRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    suspend fun getProjectOwnerId(projectId: String): String? = dbQuery {
        ProjectsTable.selectAll()
            .where { ProjectsTable.id eq projectId }
            .map { it[ProjectsTable.clientId] }
            .singleOrNull()
    }

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
            it[ProposalsTable.status] = "PENDING"
            it[ProposalsTable.createdAt] = now()
        }[ProposalsTable.id]
    }

    suspend fun getByProject(projectId: String): List<Proposal> = dbQuery {
        ProposalsTable.selectAll()
            .where { ProposalsTable.projectId eq projectId }
            .map { row ->
                Proposal(
                    id = row[ProposalsTable.id],
                    projectId = row[ProposalsTable.projectId],
                    freelancerId = row[ProposalsTable.freelancerId],
                    freelancerName = row[ProposalsTable.freelancerName],
                    freelancerRole = row[ProposalsTable.freelancerRole],
                    pitchContent = row[ProposalsTable.pitchContent],
                    budget = row[ProposalsTable.budget],
                    timelineDays = row[ProposalsTable.timelineDays],
                    projectType = row[ProposalsTable.projectType],
                    status = row[ProposalsTable.status],
                    createdAt = row[ProposalsTable.createdAt].toString(),
                )
            }
    }

    suspend fun getByFreelancer(freelancerId: String): List<Proposal> = dbQuery {
        ProposalsTable.selectAll()
            .where { ProposalsTable.freelancerId eq freelancerId }
            .map { row ->
                Proposal(
                    id = row[ProposalsTable.id],
                    projectId = row[ProposalsTable.projectId],
                    freelancerId = row[ProposalsTable.freelancerId],
                    freelancerName = row[ProposalsTable.freelancerName],
                    freelancerRole = row[ProposalsTable.freelancerRole],
                    pitchContent = row[ProposalsTable.pitchContent],
                    budget = row[ProposalsTable.budget],
                    timelineDays = row[ProposalsTable.timelineDays],
                    projectType = row[ProposalsTable.projectType],
                    status = row[ProposalsTable.status],
                    createdAt = row[ProposalsTable.createdAt].toString(),
                )
            }
    }

    suspend fun getProposalById(proposalId: Int): ProposalRecord? = dbQuery {
        ProposalsTable.selectAll()
            .where { ProposalsTable.id eq proposalId }
            .map { row ->
                ProposalRecord(
                    id = row[ProposalsTable.id],
                    projectId = row[ProposalsTable.projectId],
                    freelancerId = row[ProposalsTable.freelancerId],
                    freelancerName = row[ProposalsTable.freelancerName],
                    freelancerRole = row[ProposalsTable.freelancerRole],
                    pitchContent = row[ProposalsTable.pitchContent],
                    budget = row[ProposalsTable.budget],
                    timelineDays = row[ProposalsTable.timelineDays],
                    projectType = row[ProposalsTable.projectType],
                    status = row[ProposalsTable.status],
                    createdAt = row[ProposalsTable.createdAt].toString(),
                )
            }
            .singleOrNull()
    }

    suspend fun updateStatus(proposalId: Int, status: String): Boolean = dbQuery {
        ProposalsTable.update({ ProposalsTable.id eq proposalId }) {
            it[ProposalsTable.status] = status
        } > 0
    }

    suspend fun acceptProposalAndFormContract(
        proposalId: Int,
        clientId: String,
        freelancerId: String,
        projectId: String
    ): Boolean = dbQuery {
        val updated = ProposalsTable.update({ ProposalsTable.id eq proposalId }) {
            it[status] = "ACCEPTED"
        } > 0

        if (updated) {
            ProjectsTable.update({ ProjectsTable.id eq projectId }) {
                it[status] = "IN_PROGRESS"
            }

            val convExists = ConversationsTable.selectAll()
                .where {
                    ((ConversationsTable.user1Id eq clientId) and (ConversationsTable.user2Id eq freelancerId)) or
                    ((ConversationsTable.user1Id eq freelancerId) and (ConversationsTable.user2Id eq clientId))
                }
                .count() > 0

            if (!convExists) {
                val convId = "conv_${UUID.randomUUID().toString().replace("-", "").take(12)}"
                ConversationsTable.insert {
                    it[id] = convId
                    it[user1Id] = clientId
                    it[user2Id] = freelancerId
                    it[createdAt] = now()
                }
            }

            val notifId = NotificationsTable.insert {
                it[userId] = freelancerId
                it[type] = "MILESTONE"
                it[title] = "Proposal Accepted & Contract Formed!"
                it[description] = "Your proposal for project $projectId has been accepted. You can now begin work and collaborate directly."
                it[timestamp] = "Just now"
                it[section] = "Today"
                it[isRead] = false
                it[createdAt] = now()
            }[NotificationsTable.id]

            NotificationActionsTable.insert {
                it[notificationId] = notifId
                it[label] = "VIEW_CONTRACT"
                it[isPrimary] = true
                it[isError] = false
            }
        }
        updated
    }

    suspend fun withdraw(proposalId: Int, freelancerId: String): Boolean = dbQuery {
        ProposalsTable.deleteWhere {
            (ProposalsTable.id eq proposalId) and (ProposalsTable.freelancerId eq freelancerId)
        } > 0
    }
}
