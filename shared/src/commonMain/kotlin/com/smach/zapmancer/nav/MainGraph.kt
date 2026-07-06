package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import androidx.window.core.layout.WindowWidthSizeClass
import com.smach.zapmancer.features.alerts.screen.NotificationScreen
import com.smach.zapmancer.features.common.components.AppDrawerScaffold
import com.smach.zapmancer.features.common.components.LocalDrawerController
import com.smach.zapmancer.features.home.screen.HomeScreen
import com.smach.zapmancer.features.messages.screen.MessageDetailScreen
import com.smach.zapmancer.features.messages.screen.MessagesAdaptiveScreen
import com.smach.zapmancer.features.profile.screen.ProfileScreen
import com.smach.zapmancer.features.projects.screen.PostProjectScreen
import com.smach.zapmancer.features.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.features.projects.screen.ProjectsAdaptiveScreen
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
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isCompact = adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    val showSnackbar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    AppDrawerScaffold(
        isClientMode = isClientMode,
        onNavigateToProfile = { navigator.navigate(Screen.Profile()) },
        onNavigateToSettings = { navigator.navigate(Screen.Settings) },
        onCreateProjectClick = { navigator.navigate(Screen.PostProject) },
        onNavigateToProposal = { navigator.navigate(Screen.Proposal) },
    ) {
        val drawerController = LocalDrawerController.current
        Row(modifier = modifier) {
            if (!isCompact) {
                NavigationRail(
                    modifier = Modifier.fillMaxHeight(),
                    header = {
                        IconButton(onClick = { drawerController.open() }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    bottomNavigationRoutes.forEach { destination ->
                        NavigationRailItem(
                            selected = destination == state.topLevelRoute,
                            onClick = { navigator.navigate(destination) },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.title,
                                )
                            },
                            label = {
                                Text(
                                    text = destination.title,
                                    style = MaterialTheme.typography.labelSmall,
                                )
                            },
                        )
                    }
                }
            }

            Scaffold(
                modifier = Modifier.weight(1f),
                snackbarHost = { SnackbarHost(snackbarHostState) },
                bottomBar = {
                    if (isCompact && navigator.currentScreen.isTopLevel) {
                        BottomNavigationBar(
                            mainNavigator = navigator,
                            navigationState = state,
                        )
                    }
                },
            ) { innerPadding ->
                NavDisplay(
                    modifier = Modifier.padding(innerPadding),
                    entries = state.toEntries(appEntryProvider(navigator, showSnackbar)),
                    onBack = { navigator.goBack() },
                )
            }
        }
    }
}

/**
 * Provides the mapping between Screen keys and their corresponding Composable screens for the app.
 */
@Composable
private fun appEntryProvider(
    navigator: MainNavigator,
    showSnackbar: (String) -> Unit,
): (NavKey) -> NavEntry<NavKey> = entryProvider {
    entry<Screen.Home> {
        HomeScreen(
            onNavigateToProfile = { navigator.navigate(Screen.Profile()) },
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.ProjectList> {
        ProjectsAdaptiveScreen(
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.ProjectDetail> { key ->
        ProjectDetailScreen(
            projectId = key.id,
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.Proposal> {
        ProposalScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.Profile> { key ->
        ProfileScreen(
            userId = key.userId,
            onSearchClick = { navigator.navigate(Screen.ProjectList) },
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.Settings> {
        SettingsScreen(
            onBackClick = { navigator.goBack() },
        )
    }

    entry<Screen.PostProject> {
        PostProjectScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.ClientProposals> { key ->
        ClientProposalsScreen(
            projectId = key.projectId,
            onBackClick = { navigator.goBack() },
            onFreelancerClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.MessagesList> {
        MessagesAdaptiveScreen(
            onProfileClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.MessagesDetail> { key ->
        MessageDetailScreen(
            conversationId = key.conversationId,
            contactName = key.contactName,
            contactAvatarUrl = key.contactAvatarUrl,
            isOnline = key.isOnline,
            onBackClick = { navigator.goBack() },
            onProfileClick = { userId -> navigator.navigate(Screen.Profile(userId)) },
            showSnackbar = showSnackbar,
        )
    }

    entry<Screen.Alerts> {
        NotificationScreen(
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }
}
