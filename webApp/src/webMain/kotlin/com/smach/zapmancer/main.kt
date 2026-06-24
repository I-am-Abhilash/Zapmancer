package com.smach.zapmancer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import app.cash.sqldelight.db.SqlDriver
import com.smach.zapmancer.core.database.AppDatabase
import com.smach.zapmancer.di.initKoin
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    // Hide the HTML spinner
    document.getElementById("loading-spinner")?.setAttribute("style", "display: none;")

    // 1. Launch a Coroutine to handle the asynchronous Web browser startup
    MainScope().launch {

        // 2. Wake up Koin and save the application instance
        val koinApp = initKoin()

        // 3. Grab your SqlDriver directly from Koin's dependency graph
        val driver = koinApp.koin.get<SqlDriver>()

        // 4. Force SQLDelight to physically build the missing tables in the browser!
        // (If your IDE doesn't auto-import await(), ensure you import app.cash.sqldelight.async.coroutines.awaitCreate)
        AppDatabase.Schema.create(driver).await()

        // 5. Now that the tables exist, it is safe to boot up the UI
        ComposeViewport(document.body!!) {
            App()
        }
    }
}