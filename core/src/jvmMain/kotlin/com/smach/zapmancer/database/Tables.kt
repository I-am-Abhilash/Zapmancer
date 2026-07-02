package com.smach.zapmancer.database

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.datetime

/**
 * Shared Database Schema for the Zapmancer freelancer marketplace platform.
 * All feature modules reference tables from here to avoid circular dependencies.
 */

// ---------------------------------------------------------------------------
// Users & Auth
// ---------------------------------------------------------------------------

object UsersTable : Table("users") {
    val id = varchar("id", 128)
    val username = varchar("username", 50).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val passwordHash = varchar("password_hash", 255).nullable()
    val avatarUrl = varchar("avatar_url", 255).nullable()

    /** "FREELANCER" or "CLIENT" */
    val role = varchar("role", 20).default("FREELANCER")
    val deletedAt = datetime("deleted_at").nullable()
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

/** Stores per-user toggle preferences (1:1 with UsersTable). */
object UserSettingsTable : Table("user_settings") {
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val isTwoFactorEnabled = bool("is_two_factor_enabled").default(false)
    val isDarkModeEnabled = bool("is_dark_mode_enabled").default(true)
    val isEmailNotificationsEnabled = bool("is_email_notifications_enabled").default(true)
    val isClientModeEnabled = bool("is_client_mode_enabled").default(false)
    val organization = varchar("organization", 100).nullable()
    override val primaryKey = PrimaryKey(userId)
}

/** OTP codes for forgot-password flow. */
object OtpSessionsTable : Table("otp_sessions") {
    val id = integer("id").autoIncrement()
    val email = varchar("email", 100)
    val code = varchar("code", 10)
    val expiresAt = datetime("expires_at")
    val isUsed = bool("is_used").default(false)
    override val primaryKey = PrimaryKey(id)
}

// ---------------------------------------------------------------------------
// User Profile (extended details, 1:1 with UsersTable)
// ---------------------------------------------------------------------------

object UserProfilesTable : Table("user_profiles") {
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val roleTitle = varchar("role_title", 150).nullable()
    val location = varchar("location", 100).nullable()
    val ranking = varchar("ranking", 50).nullable()
    val isTopRated = bool("is_top_rated").default(false)
    val projectsCount = integer("projects_count").default(0)
    val rating = double("rating").default(0.0)
    val experience = varchar("experience", 50).nullable()
    val about = text("about").nullable()
    override val primaryKey = PrimaryKey(userId)
}

object ProfileSkillsTable : Table("profile_skills") {
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val skill = varchar("skill", 50)
    override val primaryKey = PrimaryKey(userId, skill)
}

object PortfolioItemsTable : Table("portfolio_items") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val title = varchar("title", 150)
    val description = text("description").nullable()
    val imageUrl = varchar("image_url", 255).nullable()
    override val primaryKey = PrimaryKey(id)
}

object ReviewsTable : Table("reviews") {
    val id = integer("id").autoIncrement()

    /** The user being reviewed (subject of the review). */
    val subjectId = varchar("subject_id", 128).references(UsersTable.id)

    /** The user who wrote the review. */
    val authorId = varchar("author_id", 128).references(UsersTable.id)
    val content = text("content")
    val rating = integer("rating").default(5)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

// ---------------------------------------------------------------------------
// Projects & Bidding
// ---------------------------------------------------------------------------

object ProjectsTable : Table("projects") {
    val id = varchar("id", 128)
    val category = varchar("category", 50)
    val title = varchar("title", 255)
    val postedTime = varchar("posted_time", 50)
    val location = varchar("location", 100).default("Remote")
    val isPaymentVerified = bool("is_payment_verified").default(false)
    val budgetRange = varchar("budget_range", 100)
    val projectType = varchar("project_type", 50).default("Fixed Price")
    val projectScope = text("project_scope").nullable()
    val timeline = varchar("timeline", 50).nullable()
    val estStart = varchar("est_start", 50).nullable()

    /** Client (owner) of this project posting. */
    val clientId = varchar("client_id", 128).references(UsersTable.id)
    val isIdentityVerified = bool("is_identity_verified").default(false)
    val isPhoneVerified = bool("is_phone_verified").default(false)
    val isClientActive = bool("is_client_active").default(true)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

object ProjectSkillsTable : Table("project_skills") {
    val projectId = varchar("project_id", 128).references(ProjectsTable.id)
    val skill = varchar("skill", 50)
    override val primaryKey = PrimaryKey(projectId, skill)
}

object ProjectDeliverablesTable : Table("project_deliverables") {
    val id = integer("id").autoIncrement()
    val projectId = varchar("project_id", 128).references(ProjectsTable.id)
    val deliverable = text("deliverable")
    override val primaryKey = PrimaryKey(id)
}

/** Tracks projects saved/bookmarked by a freelancer. */
object SavedProjectsTable : Table("saved_projects") {
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val projectId = varchar("project_id", 128).references(ProjectsTable.id)
    override val primaryKey = PrimaryKey(userId, projectId)
}

/** Tracks quick-apply actions per user per project. */
object ProjectApplicationsTable : Table("project_applications") {
    val userId = varchar("user_id", 128).references(UsersTable.id)
    val projectId = varchar("project_id", 128).references(ProjectsTable.id)
    val appliedAt = datetime("applied_at")
    override val primaryKey = PrimaryKey(userId, projectId)
}

// ---------------------------------------------------------------------------
// Proposals
// ---------------------------------------------------------------------------

object ProposalsTable : Table("proposals") {
    val id = integer("id").autoIncrement()
    val projectId = varchar("project_id", 128).references(ProjectsTable.id)
    val freelancerId = varchar("freelancer_id", 128).references(UsersTable.id)
    val freelancerName = varchar("freelancer_name", 100)
    val freelancerRole = varchar("freelancer_role", 150)
    val pitchContent = text("pitch_content")
    val budget = varchar("budget", 50)
    val timelineDays = varchar("timeline_days", 20)
    val projectType = varchar("project_type", 50).default("Fixed Price")
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

// ---------------------------------------------------------------------------
// Messaging / Chat
// ---------------------------------------------------------------------------

object ConversationsTable : Table("conversations") {
    val id = varchar("id", 128)
    val user1Id = varchar("user1_id", 128).references(UsersTable.id)
    val user2Id = varchar("user2_id", 128).references(UsersTable.id)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

object MessagesTable : Table("messages") {
    val id = varchar("id", 128)
    val conversationId = varchar("conversation_id", 128).references(ConversationsTable.id)
    val senderId = varchar("sender_id", 128).references(UsersTable.id)
    val text = text("text")

    /** "SENT", "DELIVERED", or "READ" */
    val status = varchar("status", 20).default("SENT")
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

// ---------------------------------------------------------------------------
// Notifications
// ---------------------------------------------------------------------------

object NotificationsTable : Table("notifications") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", 128).references(UsersTable.id)

    /** "MILESTONE", "MESSAGE", "ALERT", "GENERAL", or "COLLABORATOR" */
    val type = varchar("type", 50)
    val title = varchar("title", 150)
    val description = text("description")
    val timestamp = varchar("timestamp", 50)
    val section = varchar("section", 50).default("Today")
    val codeSnippet = text("code_snippet").nullable()
    val isItalic = bool("is_italic").default(false)
    val quickReply = bool("quick_reply").default(false)
    val isRead = bool("is_read").default(false)
    val createdAt = datetime("created_at")
    override val primaryKey = PrimaryKey(id)
}

object NotificationActionsTable : Table("notification_actions") {
    val id = integer("id").autoIncrement()
    val notificationId = integer("notification_id").references(NotificationsTable.id)
    val label = varchar("label", 100)
    val isPrimary = bool("is_primary").default(false)
    val isError = bool("is_error").default(false)
    override val primaryKey = PrimaryKey(id)
}
