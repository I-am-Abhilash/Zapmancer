package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.db.SqlDriver

expect fun createDatabaseDriver(): SqlDriver

internal const val DATABASE_NAME = "zapmancer.db"
