package com.smach.zapmancer.home.data

import com.smach.zapmancer.core.common.dto.RecentActivity
import com.smach.zapmancer.database.DatabaseFactory.dbQuery
import com.smach.zapmancer.database.ProjectApplicationsTable
import com.smach.zapmancer.database.ProjectsTable
import com.smach.zapmancer.database.UserSettingsTable
import com.smach.zapmancer.database.UsersTable
import com.smach.zapmancer.home.data.HomeRepository.Companion.DEFAULT_CAPACITY
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.isNull
import org.jetbrains.exposed.v1.jdbc.selectAll

class HomeRepository {

    /** Returns the display name and settings flags for the user. */
    suspend fun getUserContext(userId: String): HomeUserContext? = dbQuery {
        val userRow = UsersTable.selectAll()
            .where { UsersTable.id eq userId and UsersTable.deletedAt.isNull() }
            .singleOrNull() ?: return@dbQuery null

        val settingsRow = UserSettingsTable.selectAll()
            .where { UserSettingsTable.userId eq userId }
            .singleOrNull()

        HomeUserContext(
            name = userRow[UsersTable.username],
            isClientMode = settingsRow?.get(UserSettingsTable.isClientModeEnabled) ?: false,
        )
    }

    /** Counts how many projects the user has actively applied to. */
    suspend fun getActiveProjectsCount(userId: String): Int = dbQuery {
        ProjectApplicationsTable.selectAll()
            .where { ProjectApplicationsTable.userId eq userId }
            .count().toInt()
    }

    /**
     * Returns the maximum number of concurrent projects this user can work on.
     * Falls back to [DEFAULT_CAPACITY] until a per-user capacity column is added.
     */
    suspend fun getCapacity(userId: String): Int {
        // UserSettingsTable does not yet have a capacity column; returning a
        // sensible default until the feature is implemented.
        return DEFAULT_CAPACITY
    }

    /**
     * Returns the user's most recent applied projects as activity items.
     * In a production system, a dedicated 'activities' table would be used.
     */
    suspend fun getRecentActivities(userId: String, limit: Int = 5): List<RecentActivity> = dbQuery {
        (ProjectApplicationsTable innerJoin ProjectsTable)
            .selectAll()
            .where { ProjectApplicationsTable.userId eq userId }
            .orderBy(ProjectApplicationsTable.appliedAt, org.jetbrains.exposed.v1.core.SortOrder.DESC)
            .limit(limit)
            .map { row ->
                RecentActivity(
                    id = row[ProjectsTable.id],
                    projectName = row[ProjectsTable.title],
                    category = row[ProjectsTable.category],
                    categoryTag = row[ProjectsTable.category].take(3).uppercase(),
                    status = "IN_PROGRESS",
                    date = "Recently",
                    value = row[ProjectsTable.budgetRange],
                )
            }
    }

    companion object {
        /** Default project capacity until a per-user setting is implemented. */
        private const val DEFAULT_CAPACITY = 10
    }
}

data class HomeUserContext(val name: String, val isClientMode: Boolean)
