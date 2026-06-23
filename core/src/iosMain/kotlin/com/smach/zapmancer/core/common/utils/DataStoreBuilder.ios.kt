package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.smach.zapmancer.core.database.AppDatabase

actual fun createDatabaseDriver(): SqlDriver = NativeSqliteDriver(
    schema = AppDatabase.Schema,
    name = DATABASE_NAME,
)
