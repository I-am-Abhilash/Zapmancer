package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

@Composable
fun BottomNavigationBar(
    navController: MainNavigator,
    navigationState: NavigationState,
) {
    NavigationBar(
        modifier = Modifier.height(80.dp),
        tonalElevation = 0.dp,
        containerColor = ZapSurface,
    ) {
        val currentRoute = navigationState.topLevelRoute

        bottomNavigationRoutes.forEach { destination ->
            NavigationBarItem(
                selected = destination == currentRoute,
                onClick = {
                    navController.navigate(destination)
                },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = destination.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ZapTeal,
                    selectedTextColor = ZapTeal,
                    unselectedIconColor = Color(0xFF5F6368),
                    unselectedTextColor = Color(0xFF5F6368),
                    indicatorColor = ZapTeal.copy(alpha = 0.1f)
                )
            )

//                colors = NavigationBarItemDefaults.colors(
//                    selectedIconColor = MaterialTheme.colorScheme.primary,
//                    selectedTextColor = MaterialTheme.colorScheme.primary,
//                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
//                )
            
        }
    }
}
private val ZapTeal = Color(0xFF00897B)
private val ZapSurface = Color(0xFFFFFFFF)

/**
 * toEntries is an extension function that converts the navigation state's backstacks into
 * a list of NavEntry objects that NavDisplay can use to render the UI.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries =
        backStacks.mapValues { (_, stack) ->
            val decorators =
                listOf(
                    rememberSaveableStateHolderNavEntryDecorator<NavKey>(),
                )
            rememberDecoratedNavEntries(
                backStack = stack,
                entryDecorators = decorators,
                entryProvider = entryProvider,
            )
        }

    // Combine all active stacks (start stack + current top-level stack if different).
    return stacksInUse
        .flatMap { decoratedEntries[it] ?: emptyList() }
        .toMutableStateList()
}
