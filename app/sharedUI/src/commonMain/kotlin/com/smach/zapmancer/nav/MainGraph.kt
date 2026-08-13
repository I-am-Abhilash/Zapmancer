package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.smach.zapmancer.presentation.notifications.screen.NotificationScreen
import com.smach.zapmancer.presentation.common.adaptive.isCompactWidth
import com.smach.zapmancer.presentation.common.components.AppDrawerScaffold
import com.smach.zapmancer.presentation.common.components.LocalDrawerController
import com.smach.zapmancer.presentation.home.screen.HomeScreen
import com.smach.zapmancer.presentation.landingpage.screen.LandingPageScreen
import com.smach.zapmancer.presentation.messages.screen.MessageDetailScreen
import com.smach.zapmancer.presentation.messages.screen.MessagesAdaptiveScreen
import com.smach.zapmancer.presentation.profile.screen.EditProfileScreen
import com.smach.zapmancer.presentation.profile.screen.ProfileScreen
import com.smach.zapmancer.presentation.projects.screen.PostProjectScreen
import com.smach.zapmancer.presentation.projects.screen.ProjectDetailScreen
import com.smach.zapmancer.presentation.projects.screen.ProjectListScreen
import com.smach.zapmancer.presentation.projects.viewmodel.ProjectListViewModel
import com.smach.zapmancer.presentation.proposal.screen.ClientProposalsScreen
import com.smach.zapmancer.presentation.proposal.screen.ProposalScreen
import com.smach.zapmancer.presentation.search.screen.SearchScreen
import com.smach.zapmancer.presentation.search.viewmodel.SearchViewModel
import com.smach.zapmancer.presentation.settings.screen.SettingsScreen
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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isCompact = windowSizeClass.isCompactWidth

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
        MainAppShell(
            currentScreen = navigator.currentScreen,
            onNavigate = { destination -> navigator.navigate(destination) },
            onBackClick = { navigator.goBack() },
            onOpenDrawer = { drawerController.open() },
            modifier = modifier,
        ) { shellPadding ->
            Scaffold(
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = MaterialTheme.colorScheme.background,
            ) { scaffoldPadding ->
                NavDisplay(
                    modifier = Modifier.padding(shellPadding).padding(scaffoldPadding),
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
        MessagesAdaptiveScreen(
            onConversationClick = { conversationId ->
                navigator.navigate(
                    Screen.MessagesDetail(
                        conversationId = conversationId,
                        contactName = "",
                        contactAvatarUrl = "",
                        isOnline = false,
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

    entry<Screen.LandingPage> {
        LandingPageScreen(
            onNavigateToSearch = { navigator.navigate(Screen.Search) },
            onNavigateToProjects = { navigator.navigate(Screen.ProjectList) },
            onNavigateToLogin = { navigator.navigate(Screen.Login) },
            onNavigateToSignUp = { navigator.navigate(Screen.Signup) },
            showSnackbar = showSnackbar,
        )
    }
}
