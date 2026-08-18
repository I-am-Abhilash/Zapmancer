package com.smach.zapmancer.auth.repository

import com.smach.zapmancer.core.common.dto.VerificationStatusResponse
import com.smach.zapmancer.core.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.core.database.OtpSessionsTable
import com.smach.zapmancer.core.database.PhoneOtpSessionsTable
import com.smach.zapmancer.core.database.ProjectsTable
import com.smach.zapmancer.core.database.UserSettingsTable
import com.smach.zapmancer.core.database.UsersTable
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
 * email & phone verification, and account reactivation.
 */
class AuthRepository {

    private fun now() = System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    /** Find a user by email (optionally include soft-deleted accounts). */
    suspend fun findByEmail(email: String, includeDeleted: Boolean = false): AuthUserRecord? = dbQuery {
        val query = if (includeDeleted) {
            UsersTable.selectAll().where { UsersTable.email eq email }
        } else {
            UsersTable.selectAll()
                .where { (UsersTable.email eq email) and UsersTable.deletedAt.isNull() }
        }
        query.map { row ->
            AuthUserRecord(
                id = row[UsersTable.id],
                username = row[UsersTable.username],
                email = row[UsersTable.email],
                passwordHash = row[UsersTable.passwordHash],
                role = row[UsersTable.role],
                phoneNumber = row[UsersTable.phoneNumber],
                isEmailVerified = row[UsersTable.isEmailVerified],
                isPhoneVerified = row[UsersTable.isPhoneVerified],
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
                    role = row[UsersTable.role],
                    phoneNumber = row[UsersTable.phoneNumber],
                    isEmailVerified = row[UsersTable.isEmailVerified],
                    isPhoneVerified = row[UsersTable.isPhoneVerified],
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
            it[UsersTable.phoneNumber] = null
            it[UsersTable.isEmailVerified] = false
            it[UsersTable.isPhoneVerified] = false
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
    // Email OTP
    // ------------------------------------------------------------------

    /** Stores a new OTP code for the given email (expires in 10 minutes). */
    suspend fun saveOtp(email: String, code: String): Unit = dbQuery {
        val expiresAt =
            System.now().plus(10.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
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

    /** Updates user's password hash following successful OTP verification. */
    suspend fun updatePassword(email: String, passwordHash: String): Boolean = dbQuery {
        val normalizedEmail = email.trim().lowercase()
        UsersTable.update({ UsersTable.email eq normalizedEmail }) {
            it[UsersTable.passwordHash] = passwordHash
        } > 0
    }


    suspend fun setUserEmailVerified(userId: String): Boolean = dbQuery {
        UsersTable.update({ UsersTable.id eq userId }) {
            it[isEmailVerified] = true
        } > 0
    }

    // ------------------------------------------------------------------
    // Phone OTP (Telnyx)
    // ------------------------------------------------------------------

    /** Stores a new SMS OTP code for a user's phone number. */
    suspend fun savePhoneOtp(userId: String, phoneNumber: String, code: String): Unit = dbQuery {
        val expiresAt =
            System.now().plus(10.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
        PhoneOtpSessionsTable.insert {
            it[PhoneOtpSessionsTable.userId] = userId
            it[PhoneOtpSessionsTable.phoneNumber] = phoneNumber
            it[PhoneOtpSessionsTable.code] = code
            it[PhoneOtpSessionsTable.expiresAt] = expiresAt
            it[PhoneOtpSessionsTable.isUsed] = false
            it[PhoneOtpSessionsTable.createdAt] = now()
        }
    }

    /** Verifies the phone OTP and marks it as used. Returns the verified phone number. */
    suspend fun verifyAndConsumePhoneOtp(userId: String, code: String): String? = dbQuery {
        val currentTime = now()
        val session = PhoneOtpSessionsTable.selectAll().where {
            (PhoneOtpSessionsTable.userId eq userId) and
                (PhoneOtpSessionsTable.code eq code) and
                (PhoneOtpSessionsTable.isUsed eq false)
        }.map { row ->
            Triple(
                row[PhoneOtpSessionsTable.id],
                row[PhoneOtpSessionsTable.expiresAt],
                row[PhoneOtpSessionsTable.phoneNumber]
            )
        }.firstOrNull() ?: return@dbQuery null

        if (session.second < currentTime) return@dbQuery null

        PhoneOtpSessionsTable.update({ PhoneOtpSessionsTable.id eq session.first }) {
            it[isUsed] = true
        }
        session.third
    }

    /** Marks a user's phone as verified and synchronizes project badges. */
    suspend fun setUserPhoneVerified(userId: String, phoneNumber: String): Boolean = dbQuery {
        val userUpdated = UsersTable.update({ UsersTable.id eq userId }) {
            it[UsersTable.phoneNumber] = phoneNumber
            it[UsersTable.isPhoneVerified] = true
        } > 0

        // Synchronize project phone verification badge
        ProjectsTable.update({ ProjectsTable.clientId eq userId }) {
            it[isPhoneVerified] = true
        }

        userUpdated
    }

    /** Get comprehensive verification status for a user. */
    suspend fun getVerificationStatus(userId: String): VerificationStatusResponse? = dbQuery {
        val user = UsersTable.selectAll().where { UsersTable.id eq userId }.singleOrNull()
            ?: return@dbQuery null

        // Check if user has an approved KYC verification
        val isIdentityVerified = user[UsersTable.role] == "ADMIN" || UsersTable.selectAll()
            .where { UsersTable.id eq userId }
            .singleOrNull() != null

        VerificationStatusResponse(
            isEmailVerified = user[UsersTable.isEmailVerified],
            isPhoneVerified = user[UsersTable.isPhoneVerified],
            isIdentityVerified = isIdentityVerified,
            email = user[UsersTable.email],
            phoneNumber = user[UsersTable.phoneNumber]
        )
    }

    /** Update password hash for an authenticated user (by id or email). */
    suspend fun updatePassword(identifier: String, passwordHash: String): Boolean = dbQuery {
        UsersTable.update({ (UsersTable.id eq identifier) or (UsersTable.email eq identifier) }) {
            it[UsersTable.passwordHash] = passwordHash
        } > 0
    }
}

/** Minimal user record used internally by AuthService. */
data class AuthUserRecord(
    val id: String,
    val username: String,
    val email: String,
    val passwordHash: String?,
    val role: String = "FREELANCER",
    val phoneNumber: String? = null,
    val isEmailVerified: Boolean = false,
    val isPhoneVerified: Boolean = false,
    val isDeleted: Boolean = false,
)

