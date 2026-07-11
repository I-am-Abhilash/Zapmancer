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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.smach.zapmancer.features.messages.screen.MessagesListScreen
import com.smach.zapmancer.features.messages.viewmodel.MessagesListViewModel
import com.smach.zapmancer.features.profile.screen.EditProfileScreen
import com.smach.zapmancer.features.profile.screen.ProfileScreen
import com.smach.zapmancer.features.projects.screen.PostProjectScreen
import com.smach.zapmancer.features.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.features.projects.screen.ProjectListScreen
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import com.smach.zapmancer.features.proposal.screen.ClientProposalsScreen
import com.smach.zapmancer.features.proposal.screen.ProposalScreen
import com.smach.zapmancer.features.search.screen.SearchScreen
import com.smach.zapmancer.features.search.viewmodel.SearchViewModel
import com.smach.zapmancer.features.settings.screen.SettingsScreen
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

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
                    entries = state.toEntries(appEntryProvider(navigator, showSnackbar, onLogout)),
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
    onLogout: () -> Unit,
): (NavKey) -> NavEntry<NavKey> = entryProvider {
    entry<Screen.Home> {
        HomeScreen(
            onNavigateToProfile = { navigator.navigate(Screen.Profile()) },
            onCompleteProfileClick = { navigator.navigate(Screen.EditProfile(isNewUser = true)) },
            showSnackbar = showSnackbar,
            onSearchClick = { navigator.navigate(Screen.Search) },
            onCreateProjectClick = { navigator.navigate(Screen.PostProject) },
        )
    }

    entry<Screen.ProjectList> {
        val viewModel: ProjectListViewModel = koinViewModel()
        ProjectListScreen(
            viewModel = viewModel,
            onNavigateToProjectDetail = { id ->
                navigator.navigate(Screen.ProjectDetail(id))
            },
            onNavigateToSearch = {
                navigator.navigate(Screen.Search)
            },
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
            onNavigateToHome = { navigator.navigate(Screen.Home) },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.Profile> { key ->
        ProfileScreen(
            userId = key.userId,
            onSearchClick = { navigator.navigate(Screen.ProjectList) },
            onBackClick = { navigator.goBack() },
            onEditProfileClick = { navigator.navigate(Screen.EditProfile(isNewUser = false)) },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.EditProfile> { key ->
        EditProfileScreen(
            isNewUser = key.isNewUser,
            onBackClick = { navigator.goBack() },
            showSnackbar = showSnackbar,
        )
    }
    entry<Screen.Settings> {
        SettingsScreen(
            onBackClick = { navigator.goBack() },
            onLogoutClick = onLogout,
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
        val viewModel: MessagesListViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()
        MessagesListScreen(
            viewModel = viewModel,
            onConversationClick = { conversationId ->
                val conversation = state.conversations.find { it.id == conversationId }
                val contactName = conversation?.name.orEmpty()
                val contactAvatarUrl = conversation?.avatarUrl.orEmpty()
                val isOnline = conversation?.isOnline ?: false
                navigator.navigate(
                    Screen.MessagesDetail(
                        conversationId = conversationId,
                        contactName = contactName,
                        contactAvatarUrl = contactAvatarUrl,
                        isOnline = isOnline,
                    ),
                )
            },
            onProfileClick = { userId ->
                navigator.navigate(Screen.Profile(userId))
            },
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

    entry<Screen.Search> {
        val viewModel: SearchViewModel = koinViewModel()
        SearchScreen(
            viewModel = viewModel,
            onBackClick = { navigator.goBack() },
            onProjectClick = { id ->
                navigator.navigate(Screen.ProjectDetail(id))
            },
        )
    }
}
