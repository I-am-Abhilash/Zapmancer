package com.smach.zapmancer.users.repository

import com.smach.zapmancer.core.common.dto.PortfolioItem
import com.smach.zapmancer.core.common.dto.Review
import com.smach.zapmancer.core.common.dto.UpdateProfileRequest
import com.smach.zapmancer.core.common.dto.UserProfile
import com.smach.zapmancer.core.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.core.database.PortfolioItemsTable
import com.smach.zapmancer.core.database.ProfileSkillsTable
import com.smach.zapmancer.core.database.ReviewsTable
import com.smach.zapmancer.core.database.UserProfilesTable
import com.smach.zapmancer.core.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.core.leftJoin
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock.System

/**
 * Repository for all profile-related DB operations.
 */
class UsersRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    // ------------------------------------------------------------------
    // Profile lookup
    // ------------------------------------------------------------------

    suspend fun findProfile(userId: String): UserProfile? = dbQuery {
        val userRow = UsersTable.selectAll()
            .where { (UsersTable.id eq userId) and UsersTable.deletedAt.isNull() }
            .singleOrNull() ?: return@dbQuery null

        val profileRow = UserProfilesTable.selectAll()
            .where { UserProfilesTable.userId eq userId }
            .singleOrNull()

        val skills = ProfileSkillsTable.selectAll()
            .where { ProfileSkillsTable.userId eq userId }
            .map { it[ProfileSkillsTable.skill] }

        val portfolioItems = PortfolioItemsTable.selectAll()
            .where { PortfolioItemsTable.userId eq userId }
            .map { row ->
                PortfolioItem(
                    id = row[PortfolioItemsTable.id],
                    title = row[PortfolioItemsTable.title],
                    description = row[PortfolioItemsTable.description],
                    imageUrl = row[PortfolioItemsTable.imageUrl],
                )
            }
        val reviews = ReviewsTable
            .leftJoin(
                otherTable = UsersTable,
                onColumn = { ReviewsTable.authorId },
                otherColumn = { UsersTable.id },
            )
            .leftJoin(
                otherTable = UserProfilesTable,
                onColumn = { UsersTable.id },
                otherColumn = { UserProfilesTable.userId },
            )
            .selectAll()
            .where { ReviewsTable.subjectId eq userId }
            .map { row ->
                Review(
                    id = row[ReviewsTable.id],
                    authorId = row[ReviewsTable.authorId],
                    authorName = row[UsersTable.username],
                    authorRole = row.getOrNull(UserProfilesTable.roleTitle) ?: "",
                    content = row[ReviewsTable.content],
                    rating = row[ReviewsTable.rating],
                    authorAvatarUrl = row[UsersTable.avatarUrl],
                )
            }
        UserProfile(
            id = userRow[UsersTable.id],
            name = userRow[UsersTable.username],
            role = profileRow?.get(UserProfilesTable.roleTitle) ?: "",
            location = profileRow?.get(UserProfilesTable.location),
            ranking = profileRow?.get(UserProfilesTable.ranking),
            isTopRated = profileRow?.get(UserProfilesTable.isTopRated) ?: false,
            projectsCount = profileRow?.get(UserProfilesTable.projectsCount) ?: 0,
            rating = profileRow?.get(UserProfilesTable.rating) ?: 0.0,
            experience = profileRow?.get(UserProfilesTable.experience),
            about = profileRow?.get(UserProfilesTable.about),
            skills = skills,
            portfolioItems = portfolioItems,
            reviews = reviews,
            avatarUrl = userRow[UsersTable.avatarUrl],
        )
    }

    // ------------------------------------------------------------------
    // Profile update
    // ------------------------------------------------------------------

    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): Boolean = dbQuery {
        val newName = request.name
        val newAvatar = request.avatarUrl
        val targetSkills = request.skills

        UsersTable.update({ UsersTable.id eq userId }) {
            if (newName != null) it[username] = newName
            if (newAvatar != null) it[avatarUrl] = newAvatar
        }

        // Upsert profile row
        val exists = UserProfilesTable.selectAll()
            .where { UserProfilesTable.userId eq userId }.count() > 0

        if (exists) {
            UserProfilesTable.update({ UserProfilesTable.userId eq userId }) {
                if (request.roleTitle != null) it[roleTitle] = request.roleTitle
                if (request.location != null) it[location] = request.location
                if (request.about != null) it[about] = request.about
                if (request.experience != null) it[experience] = request.experience
            }
        } else {
            UserProfilesTable.insert {
                it[UserProfilesTable.userId] = userId
                if (request.roleTitle != null) it[roleTitle] = request.roleTitle
                if (request.location != null) it[location] = request.location
                if (request.about != null) it[about] = request.about
                if (request.experience != null) it[experience] = request.experience
            }
        }

        // Replace skills if provided
        if (targetSkills != null) {
            ProfileSkillsTable.deleteWhere {
                ProfileSkillsTable.userId eq userId
            }
            targetSkills.forEach { skill ->
                ProfileSkillsTable.insert {
                    it[ProfileSkillsTable.userId] = userId
                    it[ProfileSkillsTable.skill] = skill
                }
            }
        }
        true
    }

    // ------------------------------------------------------------------
    // Hire notification (lightweight — just records or dispatches)
    // ------------------------------------------------------------------

    suspend fun freelancerExists(freelancerId: String): Boolean = dbQuery {
        UsersTable.selectAll()
            .where { (UsersTable.id eq freelancerId) and UsersTable.deletedAt.isNull() }
            .count() > 0
    }

    // ------------------------------------------------------------------
    // Reviews
    // ------------------------------------------------------------------

    suspend fun createReview(
        authorId: String,
        subjectId: String,
        request: com.smach.zapmancer.core.common.dto.CreateReviewRequest,
    ): Review = dbQuery {
        val reviewId = ReviewsTable.insert {
            it[ReviewsTable.subjectId] = subjectId
            it[ReviewsTable.authorId] = authorId
            it[ReviewsTable.content] = request.content
            it[ReviewsTable.rating] = request.rating.coerceIn(1, 5)
            it[ReviewsTable.createdAt] = now()
        }[ReviewsTable.id]

        // Recalculate average rating for subject
        val allRatings = ReviewsTable.selectAll()
            .where { ReviewsTable.subjectId eq subjectId }
            .map { it[ReviewsTable.rating] }

        val avgRating = if (allRatings.isNotEmpty()) allRatings.average() else 0.0

        UserProfilesTable.update({ UserProfilesTable.userId eq subjectId }) {
            it[rating] = (avgRating * 10.0).toInt() / 10.0
        }

        val authorRow = UsersTable.selectAll().where { UsersTable.id eq authorId }.singleOrNull()
        val authorProfile = UserProfilesTable.selectAll().where { UserProfilesTable.userId eq authorId }.singleOrNull()

        Review(
            id = reviewId,
            authorId = authorId,
            authorName = authorRow?.get(UsersTable.username) ?: "Anonymous",
            authorRole = authorProfile?.get(UserProfilesTable.roleTitle) ?: "",
            content = request.content,
            rating = request.rating,
            authorAvatarUrl = authorRow?.get(UsersTable.avatarUrl),
        )
    }

    suspend fun getReviews(subjectId: String, limit: Int = 20, offset: Long = 0): List<Review> = dbQuery {
        ReviewsTable
            .leftJoin(
                otherTable = UsersTable,
                onColumn = { ReviewsTable.authorId },
                otherColumn = { UsersTable.id },
            )
            .leftJoin(
                otherTable = UserProfilesTable,
                onColumn = { UsersTable.id },
                otherColumn = { UserProfilesTable.userId },
            )
            .selectAll()
            .where { ReviewsTable.subjectId eq subjectId }
            .limit(limit)
            .offset(offset)
            .map { row ->
                Review(
                    id = row[ReviewsTable.id],
                    authorId = row[ReviewsTable.authorId],
                    authorName = row[UsersTable.username],
                    authorRole = row.getOrNull(UserProfilesTable.roleTitle) ?: "",
                    content = row[ReviewsTable.content],
                    rating = row[ReviewsTable.rating],
                    authorAvatarUrl = row[UsersTable.avatarUrl],
                )
            }
    }

    // ------------------------------------------------------------------
    // Account Deactivation (Soft-delete)
    // ------------------------------------------------------------------

    suspend fun deactivateAccount(userId: String): Boolean = dbQuery {
        UsersTable.update({ UsersTable.id eq userId }) {
            it[deletedAt] = now()
        } > 0
    }
}
