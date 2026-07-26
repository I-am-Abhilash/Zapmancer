// package com.smach.zapmancer.presentation.common.adaptive
//
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.PaddingValues
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.WindowInsets
// import androidx.compose.foundation.layout.fillMaxHeight
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.width
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.Add
// import androidx.compose.material.icons.filled.ChatBubble
// import androidx.compose.material.icons.filled.Home
// import androidx.compose.material.icons.filled.Notifications
// import androidx.compose.material.icons.filled.Person
// import androidx.compose.material.icons.filled.Work
// import androidx.compose.material3.DrawerValue
// import androidx.compose.material3.ExperimentalMaterial3Api
// import androidx.compose.material3.FloatingActionButton
// import androidx.compose.material3.Icon
// import androidx.compose.material3.MaterialTheme
// import androidx.compose.material3.ModalDrawerSheet
// import androidx.compose.material3.ModalNavigationDrawer
// import androidx.compose.material3.NavigationBar
// import androidx.compose.material3.NavigationBarItem
// import androidx.compose.material3.NavigationDrawerItem
// import androidx.compose.material3.NavigationDrawerItemDefaults
// import androidx.compose.material3.NavigationRail
// import androidx.compose.material3.NavigationRailItem
// import androidx.compose.material3.Scaffold
// import androidx.compose.material3.Text
// import androidx.compose.material3.rememberDrawerState
// import androidx.compose.runtime.Composable
// import androidx.compose.runtime.CompositionLocalProvider
// import androidx.compose.runtime.remember
// import androidx.compose.runtime.rememberCoroutineScope
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.graphics.vector.ImageVector
// import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.unit.dp
// import com.smach.zapmancer.presentation.common.components.DrawerController
// import com.smach.zapmancer.presentation.common.components.LocalDrawerController
// import kotlinx.coroutines.CoroutineScope
// import kotlinx.coroutines.launch
//
// /**
// * Top-level destinations in the app. The same list is used by the
// * bottom navigation bar (compact), the navigation rail (medium) and
// * the navigation drawer (expanded).
// *
// * The destination [Route] strings must match the strings used in your
// * [com.smach.zapmancer.nav.Screen] sealed class. If they don't,
// * change them here in one place.
// */
// enum class NavDestination(
//    val route: String,
//    val label: String,
//    val icon: ImageVector,
// ) {
//    Home("home", "Home", Icons.Default.Home),
//    Projects("projects", "Projects", Icons.Default.Work),
//    Messages("messages", "Messages", Icons.Default.ChatBubble),
//    Notifications("notifications", "Alerts", Icons.Default.Notifications),
//    Profile("profile", "Profile", Icons.Default.Person),
// }
//
// /**
// * Adaptive scaffold that swaps the navigation chrome based on the
// * current [WindowLayout]:
// *
// *  - [WindowLayout.Compact]  : bottom [NavigationBar] (existing
// *    hamburger-triggered drawer remains available as a fallback).
// *  - [WindowLayout.Medium]   : persistent [NavigationRail] on the
// *    leading edge.
// *  - [WindowLayout.Expanded] : permanent [ModalNavigationDrawer]
// *    (always-visible modal drawer) on the leading edge.
// *
// * The scaffold installs a [DrawerController] into [LocalDrawerController]
// * for any descendants that need to programmatically open/close the
// * drawer (compact screens still call `drawerController.open()` from
// * their existing hamburger button).
// *
// * Callers pass a [currentRoute] (the active route from your
// * `NavigationState`) and an [onNavigate] callback that updates the
// * active destination.
// */
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// fun AdaptiveScaffold(
//    currentRoute: String,
//    onNavigate: (NavDestination) -> Unit,
//    onCreateProjectClick: () -> Unit = {},
//    title: (@Composable () -> Unit)? = null,
//    content: @Composable (PaddingValues) -> Unit,
// ) {
//    val windowLayout = LocalWindowLayout.current
//    val scope = rememberCoroutineScope()
//
//    when (windowLayout) {
//        WindowLayout.Compact -> CompactScaffold(
//            currentRoute = currentRoute,
//            onNavigate = onNavigate,
//            onCreateProjectClick = onCreateProjectClick,
//            title = title,
//            content = content,
//        )
//
//        WindowLayout.Medium -> MediumScaffold(
//            currentRoute = currentRoute,
//            onNavigate = onNavigate,
//            onCreateProjectClick = onCreateProjectClick,
//            title = title,
//            content = content,
//        )
//
//        WindowLayout.Expanded -> ExpandedScaffold(
//            currentRoute = currentRoute,
//            onNavigate = onNavigate,
//            onCreateProjectClick = onCreateProjectClick,
//            title = title,
//            content = content,
//        )
//    }
// }
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// private fun CompactScaffold(
//    currentRoute: String,
//    onNavigate: (NavDestination) -> Unit,
//    onCreateProjectClick: () -> Unit,
//    title: (@Composable () -> Unit)?,
//    content: @Composable (PaddingValues) -> Unit,
// ) {
//    Scaffold(
//        topBar = { title?.invoke() },
//        bottomBar = {
//            NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
//                NavDestination.entries.forEach { dest ->
//                    NavigationBarItem(
//                        selected = currentRoute == dest.route,
//                        onClick = { onNavigate(dest) },
//                        icon = { Icon(dest.icon, contentDescription = dest.label) },
//                        label = { Text(dest.label, fontWeight = FontWeight.Medium) },
//                    )
//                }
//            }
//        },
//        floatingActionButton = {
//            if (currentRoute == NavDestination.Projects.route) {
//                FloatingActionButton(
//                    onClick = onCreateProjectClick,
//                    containerColor = MaterialTheme.colorScheme.primary,
//                    contentColor = MaterialTheme.colorScheme.onPrimary,
//                ) {
//                    Icon(Icons.Default.Add, contentDescription = "Create")
//                }
//            }
//        },
//        containerColor = MaterialTheme.colorScheme.background,
//        contentWindowInsets = WindowInsets(0),
//    ) { padding -> content(padding) }
// }
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// private fun MediumScaffold(
//    currentRoute: String,
//    onNavigate: (NavDestination) -> Unit,
//    onCreateProjectClick: () -> Unit,
//    title: (@Composable () -> Unit)?,
//    content: @Composable (PaddingValues) -> Unit,
// ) {
//    Row(modifier = Modifier.fillMaxSize()) {
//        NavigationRail(
//            modifier = Modifier.fillMaxHeight(),
//            containerColor = MaterialTheme.colorScheme.surface,
//        ) {
//            Spacer(Modifier.width(0.dp))
//            NavDestination.entries.forEach { dest ->
//                NavigationRailItem(
//                    selected = currentRoute == dest.route,
//                    onClick = { onNavigate(dest) },
//                    icon = { Icon(dest.icon, contentDescription = dest.label) },
//                    label = { Text(dest.label) },
//                )
//            }
//        }
//        Scaffold(
//            modifier = Modifier.fillMaxSize(),
//            topBar = { title?.invoke() },
//            floatingActionButton = {
//                if (currentRoute == NavDestination.Projects.route) {
//                    FloatingActionButton(
//                        onClick = onCreateProjectClick,
//                        containerColor = MaterialTheme.colorScheme.primary,
//                        contentColor = MaterialTheme.colorScheme.onPrimary,
//                    ) {
//                        Icon(Icons.Default.Add, contentDescription = "Create")
//                    }
//                }
//            },
//            containerColor = MaterialTheme.colorScheme.background,
//            contentWindowInsets = WindowInsets(0),
//        ) { padding -> content(padding) }
//    }
// }
//
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// private fun ExpandedScaffold(
//    currentRoute: String,
//    onNavigate: (NavDestination) -> Unit,
//    onCreateProjectClick: () -> Unit,
//    title: (@Composable () -> Unit)?,
//    content: @Composable (PaddingValues) -> Unit,
// ) {
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
//
//    // On expanded, the drawer is always open and doesn't need a
//    // hamburger button. We still provide a DrawerController for any
//    // caller that wants to programmatically close it (e.g. on logout).
//    val scope = rememberCoroutineScope()
//    val drawerController = remember(drawerState, scope) {
//        DrawerController(drawerState, scope)
//    }
//
//    CompositionLocalProvider(LocalDrawerController provides drawerController) {
//        ModalNavigationDrawer(
//            drawerState = drawerState,
//            drawerContent = {
//                ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
//                    NavigationDrawerBody(
//                        currentRoute = currentRoute,
//                        onNavigate = onNavigate,
//                    )
//                }
//            },
//        ) {
//            Scaffold(
//                modifier = Modifier.fillMaxSize(),
//                topBar = { title?.invoke() },
//                floatingActionButton = {
//                    if (currentRoute == NavDestination.Projects.route) {
//                        FloatingActionButton(
//                            onClick = onCreateProjectClick,
//                            containerColor = MaterialTheme.colorScheme.primary,
//                            contentColor = MaterialTheme.colorScheme.onPrimary,
//                        ) {
//                            Icon(Icons.Default.Add, contentDescription = "Create")
//                        }
//                    }
//                },
//                containerColor = MaterialTheme.colorScheme.background,
//                contentWindowInsets = WindowInsets(0),
//            ) { padding -> content(padding) }
//        }
//    }
// }
//
// /**
// * The list of destinations used inside the navigation drawer on
// * compact and expanded windows. Visually consistent with Material 3
// * `NavigationDrawerItem`s.
// */
// @OptIn(ExperimentalMaterial3Api::class)
// @Composable
// private fun NavigationDrawerBody(
//    currentRoute: String,
//    onNavigate: (NavDestination) -> Unit,
// ) {
//    ModalDrawerSheet(drawerContainerColor = MaterialTheme.colorScheme.surface) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp),
//        ) {
//            Text(
//                text = "Zapmancer",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.padding(bottom = 24.dp),
//            )
//
//            NavDestination.entries.forEach { dest ->
//                NavigationDrawerItem(
//                    label = { Text(dest.label) },
//                    selected = currentRoute == dest.route,
//                    onClick = { onNavigate(dest) },
//                    icon = { Icon(dest.icon, contentDescription = null) },
//                    colors = NavigationDrawerItemDefaults.colors(
//                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
//                        selectedTextColor = MaterialTheme.colorScheme.primary,
//                        selectedIconColor = MaterialTheme.colorScheme.primary,
//                        unselectedContainerColor = MaterialTheme.colorScheme.surface,
//                        unselectedTextColor = MaterialTheme.colorScheme.onSurface,
//                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    ),
//                    modifier = Modifier.padding(vertical = 4.dp),
//                )
//            }
//
//            Box(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(top = 16.dp),
//                contentAlignment = Alignment.BottomCenter,
//            ) {
//                Text(
//                    text = "1.0.0",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            }
//        }
//    }
// }
