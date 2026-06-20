package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import kotlinx.coroutines.launch
import com.smach.zapmancer.features.alerts.screen.NotificationScreen
import com.smach.zapmancer.features.home.screen.HomeScreen
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.messages.screen.MessageDetailScreen
import com.smach.zapmancer.features.messages.screen.MessagesListScreen
import com.smach.zapmancer.features.profile.screen.ProfileScreen
import com.smach.zapmancer.features.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.features.projects.screen.ProjectListScreen
import com.smach.zapmancer.features.proposal.screen.ProposalScreen
import com.smach.zapmancer.features.settings.screen.SettingsScreen

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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val showSnackbar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            entries = state.toEntries(appEntryProvider(navigator, showSnackbar)),
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
    showSnackbar: (String) -> Unit
): (NavKey) -> NavEntry<NavKey> = entryProvider {

    entry<Screen.Home> {
        HomeScreen(
            onCreateProjectClick = { navigator.navigate(Screen.Proposal) },
            onNavigateToSettings = { navigator.navigate(Screen.Settings) },
            onNavigateToProfile = { navigator.navigate(Screen.Profile) },
            onNavigateToProposal = { navigator.navigate(Screen.Proposal) },
            showSnackbar = showSnackbar
        )
    }

    entry<Screen.ProjectList> {
        ProjectListScreen(
            onProjectClick = { projectId ->
                navigator.navigate(Screen.ProjectDetail(id = projectId.toString()))
            }
        )
    }

    entry<Screen.ProjectDetail> { key ->
        val projectDetailKey = key as Screen.ProjectDetail
        ProjectDetailScreen(
            projectId = projectDetailKey.id,
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }

    entry<Screen.Proposal> {
        ProposalScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }
    entry<Screen.Profile> {
        ProfileScreen(
            onSearchClick = { navigator.navigate(Screen.ProjectList) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Screen.Settings> {
        SettingsScreen(
            onBackClick = { navigator.goBack() }
        )
    }

    entry<Screen.MessagesList> {
        MessagesListScreen(
            onConversationClick = { navigator.navigate(Screen.MessagesDetail) },
            onBackClick = { navigator.goBack() }
        )
    }
    entry<Screen.MessagesDetail> {
        MessageDetailScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }


    entry<Screen.Alerts> {
        NotificationScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }
}
