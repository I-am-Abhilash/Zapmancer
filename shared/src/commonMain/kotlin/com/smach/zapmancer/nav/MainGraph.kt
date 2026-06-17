package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.smach.zapmancer.features.detail.screen.DetailScreen
import com.smach.zapmancer.features.home.screen.HomeScreen
import com.smach.zapmancer.features.profile.screen.ProfileScreen
import com.smach.zapmancer.features.search.screen.SearchScreen
import com.smach.zapmancer.features.writing.ui.WritingScreen

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
            onArticleClick = { id ->
                println("Clicked article $id")
                navigator.navigate(Screen.Detail(id = id))
            },
            onSearchClick = {
                navigator.openSearchKeyboard = true
                navigator.navigate(Screen.Explore)
            }
        )
    }
    entry<Screen.Explore> {
        SearchScreen(autoFocus = navigator.openSearchKeyboard)
        navigator.openSearchKeyboard = false
    }
    entry<Screen.Writing> {
        WritingScreen(onNavigateBack = { navigator.navigate(Screen.Home) })
    }
    entry<Screen.Activity> {
        ActivityScreen()
    }
    entry<Screen.Profile> {
        ProfileScreen(
            onSearchClick = {
                navigator.openSearchKeyboard = true
                navigator.navigate(Screen.Explore)
            },
        )
    }
    entry<Screen.Detail> { screen ->
        key(screen.id) {
            DetailScreen(
                articleId = screen.id,
                onBack = { navigator.goBack() }
            )
        }
    }
}

@Composable
fun ActivityScreen() {
    // TODO: Implement Activity Screen
}
