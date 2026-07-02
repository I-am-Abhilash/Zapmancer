package com.smach.zapmancer.settings.data

import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.UserSettingsTable
import com.smach.zapmancer.database.UsersTable
import com.smach.zapmancer.core.common.dto.*
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update

class SettingsRepository {

    private val appVersion = "v1.0.0 // ZAPMANCER_CORE"

    suspend fun getSettings(userId: String): SettingsData? = dbQuery {
        val userRow = UsersTable.selectAll().where { UsersTable.id eq userId }.singleOrNull()
            ?: return@dbQuery null

        val settingsRow = UserSettingsTable.selectAll()
            .where { UserSettingsTable.userId eq userId }.singleOrNull()

        SettingsData(
            email = userRow[UsersTable.email],
            organization = settingsRow?.get(UserSettingsTable.organization),
            isTwoFactorEnabled = settingsRow?.get(UserSettingsTable.isTwoFactorEnabled) ?: false,
            isEmailNotificationsEnabled = settingsRow?.get(UserSettingsTable.isEmailNotificationsEnabled) ?: true,
            version = appVersion,
            isClientModeEnabled = settingsRow?.get(UserSettingsTable.isClientModeEnabled) ?: false,
        )
    }

    suspend fun updateToggle(userId: String, field: SettingsField, enabled: Boolean): Boolean = dbQuery {
        ensureSettingsRow(userId)
        UserSettingsTable.update({ UserSettingsTable.userId eq userId }) {
            when (field) {
                SettingsField.TWO_FA -> it[UserSettingsTable.isTwoFactorEnabled] = enabled
                SettingsField.EMAIL_NOTIFS -> it[UserSettingsTable.isEmailNotificationsEnabled] = enabled
                SettingsField.CLIENT_MODE -> it[UserSettingsTable.isClientModeEnabled] = enabled
            }
        } > 0
    }

    private fun ensureSettingsRow(userId: String) {
        val exists = UserSettingsTable.selectAll()
            .where { UserSettingsTable.userId eq userId }.count() > 0
        if (!exists) {
            UserSettingsTable.insert { it[UserSettingsTable.userId] = userId }
        }
    }
}

enum class SettingsField { TWO_FA, EMAIL_NOTIFS, CLIENT_MODE }
