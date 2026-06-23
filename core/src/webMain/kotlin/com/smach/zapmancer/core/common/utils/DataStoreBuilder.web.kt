package com.smach.zapmancer.core.common.utils

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.worker.WebWorkerDriver
import org.w3c.dom.Worker

actual fun createDatabaseDriver(): SqlDriver {
    val worker = Worker(
        // ❌ WRONG (My mistake): "@cashapp/sqldelight-sqljs-worker/worker.mjs"
        // ✅ CORRECT: "@cashapp/sqldelight-sqljs-worker/sqljs.worker.js"
        js("""new URL("@cashapp/sqldelight-sqljs-worker/sqljs.worker.js", import.meta.url)""")
    )

    return WebWorkerDriver(worker)
}
