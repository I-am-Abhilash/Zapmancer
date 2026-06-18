package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.smach.zapmancer.features.alerts.screen.NotificationScreen
import com.smach.zapmancer.features.alerts.state.NotificationUiState
import com.smach.zapmancer.features.home.screen.HomeContent
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.messages.screen.MessageDetailScreen
import com.smach.zapmancer.features.profile.screen.ProfileContent
import com.smach.zapmancer.features.profile.state.ProfileUiState
import com.smach.zapmancer.features.projects.screen.ProjectPortfolioContent

/**
 * MainGraph is the entry point for authenticated app content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGraph(
    modifier: Modifier = Modifier,
) {
    val state = rememberNavigationState(Screen.Home, bottomNavigationRoutes)
    val navigator = MainNavigator(state)

    Scaffold(
        modifier = modifier,
        bottomBar = {
            if (navigator.currentScreen.isTopLevel) {
                BottomNavigationBar(
                    navController = navigator,
                    navigationState = state,
                )
            }
        },
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            entries = state.toEntries(appEntryProvider(navigator)),
            onBack = {
                navigator.goBack()
            },
        )
    }
}

/**
 * Provides the mapping between Screen keys and their corresponding Composable screens for the app.
 */
@Composable
private fun appEntryProvider(
    navigator: MainNavigator,
): (NavKey) -> NavEntry<NavKey> = entryProvider {
    
    entry<Screen.Home> {
        HomeContent(
            state = HomeUiState(),
            onCreateProjectClick = { navigator.navigate(Screen.ProjectList) }
        )
    }

    entry<Screen.ProjectList> {
        ProjectPortfolioContent(
            onSearchClick = { /* TODO */ },
            onProjectClick = { projectId ->
                // navigator.navigate(Screen.ProjectDetail(projectId))
            }
        )
    }
    entry<Screen.Profile> {
        ProfileContent(
            state = ProfileUiState(),
            onEvent = {  }
        )
    }
    entry<Screen.Messages> {
        MessageDetailScreen()
    }

    entry<Screen.Alerts> {
        NotificationScreen(
            state = NotificationUiState(),
            onBackClick = { navigator.goBack() },
            onFilterClick = {}
        )
    }
}

@Composable
fun ActivityScreen() {
    // TODO: Implement Activity Screen
}
