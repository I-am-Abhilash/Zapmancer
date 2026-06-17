package com.smach.zapmancer.nav

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator

/**
 * BottomNavigationBar renders the bottom navigation bar with items for top-level routes.
 */
@Composable
fun BottomNavigationBar(
    navController: MainNavigator,
    navigationState: NavigationState,
) {
    BottomAppBar {
        val topLevelRoute = navigationState.topLevelRoute

        bottomNavigationRoutes.forEach { destination ->
            val selected = destination == topLevelRoute

            BottomNavItem(
                isSelected = selected,
                destination = destination,
                onClick = {
                    navController.navigate(destination)
                },
            )
        }
    }
}

/**
 * toEntries is an extension function that converts the navigation state's backstacks into
 * a list of NavEntry objects that NavDisplay can use to render the UI.
 */
@Composable
fun NavigationState.toEntries(entryProvider: (NavKey) -> NavEntry<NavKey>): SnapshotStateList<NavEntry<NavKey>> {
    val decoratedEntries =
        backStacks.mapValues { (_, stack) ->
            // Use rememberSaveableStateHolderNavEntryDecorator to preserve state across backstack changes.
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

/**
 * BottomNavItem represents a single clickable item in the BottomNavigationBar.
 */
@Composable
fun RowScope.BottomNavItem(
    isSelected: Boolean,
    destination: Screen,
    onClick: () -> Unit,
) {
    val iconColor =
        if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }

    Column(
        modifier =
            Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(
                    onClick = onClick,
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    )
    {
        Icon(
            imageVector = destination.icon,
            contentDescription = destination.title,
            tint = iconColor,
        )
        Spacer(modifier = Modifier.height(6.dp))

        Text(
            modifier = Modifier,
            text = destination.title,
            style = MaterialTheme.typography.labelSmall,
            color = iconColor,
        )
    }
}
