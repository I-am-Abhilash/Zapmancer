package com.smach.zapmancer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.smach.zapmancer.di.initKoin
import kotlinx.browser.document
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    document.getElementById("loading-spinner")?.setAttribute("style", "display: none;")

    MainScope().launch {

        val koinApp = initKoin()

        ComposeViewport(document.body!!) {
            App()
        }
    }
}