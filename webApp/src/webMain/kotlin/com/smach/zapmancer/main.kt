package com.smach.zapmancer

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.smach.zapmancer.di.initKoin
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    document.getElementById("loading-spinner")?.setAttribute("style", "display: none;")

    initKoin()

    ComposeViewport(document.body!!) {
        App()
    }
}
