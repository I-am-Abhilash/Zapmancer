package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.db.SqlDriver

actual fun createDatabaseDriver(): SqlDriver =
    throw UnsupportedOperationException("SqlDelight database driver is not supported/needed on the JVM target.")
