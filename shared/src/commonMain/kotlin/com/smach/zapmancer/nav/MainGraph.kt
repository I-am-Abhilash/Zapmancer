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
import com.smach.zapmancer.features.home.screen.HomeScreen
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.messages.screen.MessageDetailScreen
import com.smach.zapmancer.features.messages.screen.MessagesListScreen
import com.smach.zapmancer.features.messages.state.MessagesDetailUiState
import com.smach.zapmancer.features.messages.state.MessagesListUiState
import com.smach.zapmancer.features.profile.screen.ProfileContent
import com.smach.zapmancer.features.profile.state.ProfileUiState
import com.smach.zapmancer.features.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.features.projects.screen.ProjectListContent
import com.smach.zapmancer.features.proposal.screen.ProposalScreen

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
        HomeScreen(
            state = HomeUiState(),
            onCreateProjectClick = { navigator.navigate(Screen.Proposal)}
        )
    }

    entry<Screen.ProjectList> {
        ProjectListContent(
            onSearchClick = { /* TODO */ },
            onProjectClick = { navigator.navigate(Screen.ProjectDetail)}
//            onProjectClick = { projectId ->
//                // navigator.navigate(Screen.ProjectDetail(projectId))
//            }
        )
    }

    entry<Screen.ProjectDetail> {
        ProjectDetailScreen()
    }

    entry<Screen.Proposal> {
        ProposalScreen()

    }
    entry<Screen.Profile> {
        ProfileContent(
            state = ProfileUiState(),
            onEvent = { }
        )
    }
    entry<Screen.Settings> {
        ProfileContent(
            state = ProfileUiState(),
            onEvent = { }
        )
    }

    entry<Screen.MessagesList> {
        MessagesListScreen(
            state = MessagesListUiState(),
            onConversationClick = { navigator.navigate(Screen.MessagesDetail)},
            onBackClick = {navigator.goBack()}
        )
    }
    entry<Screen.MessagesDetail> {
        MessageDetailScreen(
            state = MessagesDetailUiState(),
            onBackClick = {navigator.goBack()},
        )
    }


    entry<Screen.Alerts> {
        NotificationScreen(
            state = NotificationUiState(),
            onBackClick = { navigator.goBack() },
            onFilterClick = {}
        )
    }
}
