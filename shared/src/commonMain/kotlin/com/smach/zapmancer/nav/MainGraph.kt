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
import com.smach.zapmancer.features.alerts.screen.NotificationScreen
import com.smach.zapmancer.features.common.components.AppDrawerScaffold
import com.smach.zapmancer.features.home.screen.HomeScreen
import com.smach.zapmancer.features.messages.screen.MessageDetailScreen
import com.smach.zapmancer.features.messages.screen.MessagesListScreen
import com.smach.zapmancer.features.profile.screen.ProfileScreen
import com.smach.zapmancer.features.projects.screen.PostProjectScreen
import com.smach.zapmancer.features.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.features.projects.screen.ProjectListScreen
import com.smach.zapmancer.features.proposal.screen.ClientProposalsScreen
import com.smach.zapmancer.features.proposal.screen.ProposalScreen
import com.smach.zapmancer.features.settings.screen.SettingsScreen
import kotlinx.coroutines.launch

/**
 * MainGraph is the entry point for authenticated app content.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainGraph(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit = {},
    isClientMode: Boolean,
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
        AppDrawerScaffold(
            isClientMode = isClientMode,
            onNavigateToProfile = { navigator.navigate(Screen.Profile()) },
            onNavigateToSettings = { navigator.navigate(Screen.Settings) },
            onCreateProjectClick = { navigator.navigate(Screen.PostProject) },
            onNavigateToProposal = { navigator.navigate(Screen.Proposal) }
        ) {
            NavDisplay(
                modifier = Modifier.padding(innerPadding),
                entries = state.toEntries(appEntryProvider(navigator, showSnackbar)),
                onBack = { navigator.goBack() },
            )
        }
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
            onNavigateToProfile = { navigator.navigate(Screen.Profile()) },
            showSnackbar = showSnackbar
        )
    }

    entry<Screen.ProjectList> {
        ProjectListScreen(
            onProjectClick = { projectId ->
                navigator.navigate(Screen.ProjectDetail(id = projectId.toString()))
            },
            onEvent = {}
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
    entry<Screen.Profile> { key ->
        val profileKey = key as Screen.Profile
        ProfileScreen(
            userId = profileKey.userId,
            onSearchClick = { navigator.navigate(Screen.ProjectList) },
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }
    entry<Screen.Settings> {
        SettingsScreen(
            onBackClick = { navigator.goBack() }
        )
    }

    entry<Screen.PostProject> {
        PostProjectScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar
        )
    }

    entry<Screen.ClientProposals> { key ->
        val proposalsKey = key as Screen.ClientProposals
        ClientProposalsScreen(
            projectId = proposalsKey.projectId,
            onBackClick = { navigator.goBack() },
            onFreelancerClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
            showSnackbar = showSnackbar
        )
    }

    entry<Screen.MessagesList> {
        MessagesListScreen(
            onConversationClick = { navigator.navigate(Screen.MessagesDetail) },
            onProfileClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
        )
    }
    entry<Screen.MessagesDetail> {
        MessageDetailScreen(
            onBackClick = { navigator.goBack() },
            onProfileClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
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
