package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

actual fun createDatabaseDriver(): SqlDriver {
    return WebWorkerDriver(
        Worker("sqldelight-worker.js")
    )
}
