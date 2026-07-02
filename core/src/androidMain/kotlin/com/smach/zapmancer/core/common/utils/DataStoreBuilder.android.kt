package com.smach.zapmancer.core.common.utils

import android.content.Context
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.smach.zapmancer.core.database.AppDatabase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidDatabaseDriverBuilder : KoinComponent {
    private val context: Context by inject()

    fun create(): SqlDriver = AndroidSqliteDriver(
        AppDatabase.Schema.synchronous(),
        context,
        DATABASE_NAME,
    )
}

actual fun createDatabaseDriver(): SqlDriver = AndroidDatabaseDriverBuilder().create()
