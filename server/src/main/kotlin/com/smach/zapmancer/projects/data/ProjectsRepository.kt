package com.smach.zapmancer.projects.data

import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.ProjectApplicationsTable
import com.smach.zapmancer.database.ProjectDeliverablesTable
import com.smach.zapmancer.database.ProjectSkillsTable
import com.smach.zapmancer.database.ProjectsTable
import com.smach.zapmancer.database.SavedProjectsTable
import com.smach.zapmancer.database.UserProfilesTable
import com.smach.zapmancer.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.SortOrder
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import java.util.UUID
import kotlin.time.Clock.System

class ProjectsRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    suspend fun getAllProjects(userId: String): List<Project> = dbQuery {
        ProjectsTable.selectAll()
            .orderBy(ProjectsTable.createdAt, SortOrder.DESC)
            .map { row ->
                val projectId = row[ProjectsTable.id]
                val skills = getSkills(projectId)
                val isSaved = isSavedByUser(userId, projectId)
                Project(
                    id = projectId,
                    category = row[ProjectsTable.category],
                    title = row[ProjectsTable.title],
                    postedTime = row[ProjectsTable.postedTime],
                    location = row[ProjectsTable.location],
                    isPaymentVerified = row[ProjectsTable.isPaymentVerified],
                    budgetRange = row[ProjectsTable.budgetRange],
                    projectType = row[ProjectsTable.projectType],
                    skills = skills,
                    isSaved = isSaved,
                )
            }
    }

    suspend fun findById(projectId: String, userId: String): ProjectDetail? = dbQuery {
        val row = ProjectsTable.selectAll()
            .where { ProjectsTable.id eq projectId }
            .singleOrNull() ?: return@dbQuery null

        val skills = getSkills(projectId)
        val deliverables = getDeliverables(projectId)
        val isSaved = isSavedByUser(userId, projectId)
        val clientId = row[ProjectsTable.clientId]

        val clientRow = UsersTable.selectAll().where { UsersTable.id eq clientId }.singleOrNull()
        val clientProfile = UserProfilesTable.selectAll()
            .where { UserProfilesTable.userId eq clientId }.singleOrNull()

        ProjectDetail(
            id = projectId,
            category = row[ProjectsTable.category],
            title = row[ProjectsTable.title],
            postedTime = row[ProjectsTable.postedTime],
            location = row[ProjectsTable.location],
            isPaymentVerified = row[ProjectsTable.isPaymentVerified],
            projectScope = row[ProjectsTable.projectScope],
            deliverables = deliverables,
            skills = skills,
            budgetRange = row[ProjectsTable.budgetRange],
            projectType = row[ProjectsTable.projectType],
            timeline = row[ProjectsTable.timeline],
            estStart = row[ProjectsTable.estStart],
            clientName = clientRow?.get(UsersTable.username) ?: "Unknown",
            clientIndustry = clientProfile?.get(UserProfilesTable.roleTitle) ?: "Unknown",
            clientLocation = clientProfile?.get(UserProfilesTable.location) ?: "Unknown",
            clientProjectsCount = clientProfile?.get(UserProfilesTable.projectsCount) ?: 0,
            clientRating = clientProfile?.get(UserProfilesTable.rating) ?: 0.0,
            isSaved = isSaved,
            isClientActive = row[ProjectsTable.isClientActive],
            isIdentityVerified = row[ProjectsTable.isIdentityVerified],
            isPhoneVerified = row[ProjectsTable.isPhoneVerified],
        )
    }

    suspend fun toggleSave(userId: String, projectId: String, save: Boolean): Boolean = dbQuery {
        val exists = SavedProjectsTable.selectAll()
            .where { (SavedProjectsTable.userId eq userId) and (SavedProjectsTable.projectId eq projectId) }
            .count() > 0
        if (save && !exists) {
            SavedProjectsTable.insert {
                it[SavedProjectsTable.userId] = userId
                it[SavedProjectsTable.projectId] = projectId
            }
        } else if (!save && exists) {
            SavedProjectsTable.deleteWhere {
                (SavedProjectsTable.userId eq userId) and (SavedProjectsTable.projectId eq projectId)
            }
        }
        true
    }

    suspend fun apply(userId: String, projectId: String): Boolean = dbQuery {
        val exists = ProjectApplicationsTable.selectAll()
            .where { (ProjectApplicationsTable.userId eq userId) and (ProjectApplicationsTable.projectId eq projectId) }
            .count() > 0
        if (!exists) {
            ProjectApplicationsTable.insert {
                it[ProjectApplicationsTable.userId] = userId
                it[ProjectApplicationsTable.projectId] = projectId
                it[ProjectApplicationsTable.appliedAt] = now()
            }
        }
        true
    }

    suspend fun create(clientId: String, request: CreateProjectRequest): String = dbQuery {
        val id = "proj_${UUID.randomUUID().toString().replace("-", "").take(12)}"
        ProjectsTable.insert {
            it[ProjectsTable.id] = id
            it[ProjectsTable.category] = request.category
            it[ProjectsTable.title] = request.title
            it[ProjectsTable.postedTime] = "Just now"
            it[ProjectsTable.location] = request.location
            it[ProjectsTable.budgetRange] = request.budgetRange
            it[ProjectsTable.projectType] = request.projectType
            it[ProjectsTable.projectScope] = request.projectScope
            it[ProjectsTable.timeline] = request.timeline
            it[ProjectsTable.estStart] = request.estStart
            it[ProjectsTable.clientId] = clientId
            it[ProjectsTable.createdAt] = now()
        }
        request.skills.forEach { skill ->
            ProjectSkillsTable.insert {
                it[ProjectSkillsTable.projectId] = id
                it[ProjectSkillsTable.skill] = skill
            }
        }
        request.deliverables.forEach { d ->
            ProjectDeliverablesTable.insert {
                it[ProjectDeliverablesTable.projectId] = id
                it[ProjectDeliverablesTable.deliverable] = d
            }
        }
        id
    }

    private fun getSkills(projectId: String): List<String> = ProjectSkillsTable.selectAll()
        .where { ProjectSkillsTable.projectId eq projectId }
        .map { it[ProjectSkillsTable.skill] }

    private fun getDeliverables(projectId: String): List<String> = ProjectDeliverablesTable.selectAll()
        .where { ProjectDeliverablesTable.projectId eq projectId }
        .map { it[ProjectDeliverablesTable.deliverable] }

    private fun isSavedByUser(userId: String, projectId: String): Boolean = SavedProjectsTable.selectAll()
        .where { (SavedProjectsTable.userId eq userId) and (SavedProjectsTable.projectId eq projectId) }
        .count() > 0
}
