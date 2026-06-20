package com.smach.zapmancer

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.smach.zapmancer.domain.repository.SettingsRepository
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.nav.MainGraph
import org.koin.compose.koinInject

@Composable
fun App() {
    val settingsRepository: SettingsRepository = koinInject()
    val settingsState by settingsRepository.settingsFlow.collectAsState(initial = null)
    val isDarkMode = settingsState?.isDarkModeEnabled ?: isSystemInDarkTheme()

    AppTheme(darkTheme = isDarkMode) {
        MainGraph()
    }
}
