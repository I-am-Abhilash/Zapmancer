package com.smach.zapmancer.auth.data

import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.OtpSessionsTable
import com.smach.zapmancer.database.UserSettingsTable
import com.smach.zapmancer.database.UsersTable
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import kotlin.time.Clock.System
import kotlin.time.Duration.Companion.minutes

/**
 * AuthRepository owns all DB access needed exclusively by AuthService:
 * user creation, lookup by email/id, password hash retrieval, OTP management,
 * and account reactivation.
 */
class AuthRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    /** Find a user by email (optionally include soft-deleted accounts). */
    suspend fun findByEmail(email: String, includeDeleted: Boolean = false): AuthUserRecord? = dbQuery {
        val query = if (includeDeleted) {
            UsersTable.selectAll().where { UsersTable.email eq email }
        } else {
            UsersTable.selectAll().where { (UsersTable.email eq email) and UsersTable.deletedAt.isNull() }
        }
        query.map { row ->
            AuthUserRecord(
                id = row[UsersTable.id],
                username = row[UsersTable.username],
                email = row[UsersTable.email],
                passwordHash = row[UsersTable.passwordHash],
                isDeleted = row[UsersTable.deletedAt] != null,
            )
        }.singleOrNull()
    }

    /** Find a user by ID. */
    suspend fun findById(id: String): AuthUserRecord? = dbQuery {
        UsersTable.selectAll().where { (UsersTable.id eq id) and UsersTable.deletedAt.isNull() }
            .map { row ->
                AuthUserRecord(
                    id = row[UsersTable.id],
                    username = row[UsersTable.username],
                    email = row[UsersTable.email],
                    passwordHash = row[UsersTable.passwordHash],
                    isDeleted = row[UsersTable.deletedAt] != null,
                )
            }.singleOrNull()
    }

    /** Check if a username is already taken. */
    suspend fun usernameExists(username: String): Boolean = dbQuery {
        UsersTable.selectAll().where { UsersTable.username eq username }.count() > 0
    }

    /**
     * Creates a new user record and a default settings row.
     * Returns the created user's id.
     */
    suspend fun createUser(
        id: String,
        username: String,
        email: String,
        passwordHash: String,
    ): String = dbQuery {
        UsersTable.insert {
            it[UsersTable.id] = id
            it[UsersTable.username] = username
            it[UsersTable.email] = email
            it[UsersTable.passwordHash] = passwordHash
            it[UsersTable.createdAt] = now()
        }
        UserSettingsTable.insert {
            it[UserSettingsTable.userId] = id
        }
        id
    }

    /** Reactivate a soft-deleted account (called on login during cooling period). */
    suspend fun reactivateAccount(id: String): Boolean = dbQuery {
        UsersTable.update({ UsersTable.id eq id }) {
            it[deletedAt] = null
        } > 0
    }

    // ------------------------------------------------------------------
    // OTP
    // ------------------------------------------------------------------

    /** Stores a new OTP code for the given email (expires in 10 minutes). */
    suspend fun saveOtp(email: String, code: String): Unit = dbQuery {
        val expiresAt = System.now().plus(10.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
        OtpSessionsTable.insert {
            it[OtpSessionsTable.email] = email
            it[OtpSessionsTable.code] = code
            it[OtpSessionsTable.expiresAt] = expiresAt
        }
    }

    /** Verifies the OTP and marks it as used. Returns true if valid and not expired. */
    suspend fun verifyAndConsumeOtp(email: String, code: String): Boolean = dbQuery {
        val currentTime = now()
        val session = OtpSessionsTable.selectAll().where {
            (OtpSessionsTable.email eq email) and
                (OtpSessionsTable.code eq code) and
                (OtpSessionsTable.isUsed eq false)
        }.map { row ->
            Pair(row[OtpSessionsTable.id], row[OtpSessionsTable.expiresAt])
        }.firstOrNull() ?: return@dbQuery false

        if (session.second < currentTime) return@dbQuery false

        OtpSessionsTable.update({ OtpSessionsTable.id eq session.first }) {
            it[isUsed] = true
        }
        true
    }
}

/** Minimal user record used internally by AuthService. */
data class AuthUserRecord(
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String?,
    val isDeleted: Boolean,
)
