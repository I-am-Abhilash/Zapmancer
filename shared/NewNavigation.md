To achieve this, we need to combine the `NavigationSuiteScaffold` (which handles the Bottom Bar and Navigation Rail) with a standard `Scaffold` (which handles the Top Bar).

However, there is an important architectural best practice you need to know first: **Navigation components should not live inside individual screens.**

If you put the bottom bar and web navigation directly inside `NotificationContent`, you will have to copy and paste that exact same navigation code into `HomeContent`, `ProfileContent`, and `SettingsContent`.

Instead, you should create an **App Shell** (a master wrapper) that handles the navigation, and pass your screens into it.

Here is how you refactor your code into a proper App Shell architecture to achieve exactly what you want.

### 1. Create the App Shell (The Master Layout)

This wrapper checks the window size.

* If it is a **Phone/Tablet**, it uses the BottomBar/NavRail and your standard `ZapmancerTopBar`.
* If it is **Web (Expanded)**, it hides the default navigation and uses a custom website-style Top Bar instead.

```kotlin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun MainAppShell(
    currentRoute: String, // e.g., "home", "notifications"
    onNavigate: (String) -> Unit,
    onBackClick: () -> Unit,
    // This is where your individual screens will be injected
    content: @Composable (PaddingValues) -> Unit 
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isWeb = adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    // 1. Calculate Layout: Hide standard Nav Suite on Web
    val layoutType = if (isWeb) {
        NavigationSuiteType.None
    } else {
        NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
    }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            // These automatically render as a Bottom Bar on phones, and a Nav Rail on Tablets.
            // (They are hidden entirely on Web because of layoutType = None)
            item(
                selected = currentRoute == "home",
                onClick = { onNavigate("home") },
                icon = { Icon(Icons.Default.Home, "Home") },
                label = { Text("Home") }
            )
            item(
                selected = currentRoute == "notifications",
                onClick = { onNavigate("notifications") },
                icon = { Icon(Icons.Outlined.Notifications, "Notifications") },
                label = { Text("Notifications") }
            )
        }
    ) {
        // 2. The Inner Scaffold handles the Top Bar
        Scaffold(
            topBar = {
                if (isWeb) {
                    // WEB MODE: Traditional Website Header Navigation
                    TopAppBar(
                        title = { Text("Zapmancer Web") },
                        actions = {
                            TextButton(onClick = { onNavigate("home") }) { Text("Home") }
                            TextButton(onClick = { onNavigate("notifications") }) { Text("Notifications") }
                        }
                    )
                } else {
                    // MOBILE/TABLET MODE: Your standard Top Bar
                    ZapmancerTopBar(
                        title = "Zapmancer",
                        showBackButton = false,
                        onBackClick = onBackClick,
                        containerColor = MaterialTheme.colorScheme.surface,
                        drawBottomBorder = true,
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
        ) { padding ->
            // 3. Render the actual screen content (Notifications, Home, etc.)
            content(padding)
        }
    }
}

```

### 2. Strip Down `NotificationContent`

Because the `MainAppShell` is now handling the `Scaffold` and the top bars, your `NotificationContent` becomes incredibly simple. It only has to worry about rendering its specific data inside the `AdaptiveCenterContainer` you built earlier.

```kotlin
@Composable
fun NotificationContent(
    state: NotificationUiState,
    onEvent: (NotificationEvent) -> Unit,
    // Accept the padding from the parent App Shell
    paddingValues: PaddingValues 
) {
    // 1. Notice we removed the Scaffold completely! 
    // The wrapper handles it now.
    
    AdaptiveCenterContainer(
        modifier = Modifier.padding(paddingValues), // Apply the shell padding
        maxWidth = 840.dp,
        alignment = Alignment.TopCenter
    ) {
        if (state.notifications.isEmpty() && !state.isLoading && state.error == null) {
            EmptyState(
                title = "All caught up!",
                description = "You have no unread notifications or tasks requiring action.",
                icon = Icons.Outlined.Notifications,
                buttonText = "Refresh",
                onButtonClick = { onEvent(NotificationEvent.Refresh) },
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 24.dp, bottom = 100.dp),
            ) {
                val grouped = state.notifications.groupBy { it.section }
                grouped.forEach { (section, items) ->
                    item { RibbonHeader(section) }
                    items(items) { item ->
                        NotificationCard(
                            item = item,
                            replyText = state.replyDrafts[item.id] ?: "",
                            onReplyTextChanged = { text ->
                                onEvent(NotificationEvent.OnReplyTextChanged(item.id, text))
                            },
                            onSendReply = { onEvent(NotificationEvent.SendQuickReply(item.id)) },
                            onActionClicked = { actionLabel ->
                                onEvent(NotificationEvent.ExecuteAction(item.id, actionLabel))
                            },
                        )
                    }
                }
            }
        }
    }
}

```

### 3. Tie them together in your NavHost

When you set up your Jetpack Compose Navigation, you wrap all your screens inside the App Shell. This guarantees every screen instantly has perfect responsive navigation.

```kotlin
@Composable
fun ZapmancerApp() {
    val navController = rememberNavController()
    // Track current route to pass to the shell
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"

    MainAppShell(
        currentRoute = currentRoute,
        onNavigate = { route -> navController.navigate(route) },
        onBackClick = { navController.popBackStack() }
    ) { shellPadding -> 
        
        NavHost(navController = navController, startDestination = "notifications") {
            composable("notifications") {
                // Get your state here...
                NotificationContent(
                    state = myNotificationState,
                    onEvent = { /* Handle events */ },
                    paddingValues = shellPadding // Pass the padding down!
                )
            }
            // Add other screens here...
        }
    }
}

```