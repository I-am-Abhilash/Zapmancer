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
    document.getElementById("loading-spinner")?.setAttribute("style", "display: none;")
    MainScope().launch {
        val koinApp = initKoin()
        val driver = koinApp.koin.get<SqlDriver>()
        AppDatabase.Schema.create(driver).await()
        ComposeViewport(document.body!!) {
            App()
        }
    }
}
