package com.smach.zapmancer.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass

/**
 * MainAppShell is the master adaptive wrapper component.
 *
 * It uses Compose Material3 Adaptive NavigationSuiteScaffold:
 * - Phone (COMPACT): Bottom Navigation Bar + Top Bar
 * - Tablet (MEDIUM): Side Navigation Rail + Top Bar
 * - Web/Desktop (EXPANDED): Top Navigation Header with horizontal destination tabs (no bottom bar or rail)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppShell(
    currentScreen: Screen,
    onNavigate: (Screen) -> Unit,
    onBackClick: () -> Unit,
    onOpenDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit,
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isExpanded =
        adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED

    val layoutType =
        if (isExpanded) {
            NavigationSuiteType.None
        } else {
            NavigationSuiteScaffoldDefaults.calculateFromAdaptiveInfo(adaptiveInfo)
        }

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            bottomNavigationRoutes.forEach { destination ->
                item(
                    selected = destination == currentScreen,
                    onClick = { onNavigate(destination) },
                    icon = {
                        Icon(
                            imageVector = destination.icon,
                            contentDescription = destination.title,
                        )
                    },
                    label = { Text(destination.title) },
                )
            }
        },
        modifier = modifier,
    ) {
        Scaffold(
            topBar = {
                if (isExpanded) {
                    // EXPANDED (WEB): Single Web Top Navigation Header with logo, drawer trigger, and tabs
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                    ) {
                        Row(
                            modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(onClick = onOpenDrawer) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Open menu",
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Zapmancer",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                bottomNavigationRoutes.forEach { destination ->
                                    val isSelected = destination == currentScreen
                                    TextButton(
                                        onClick = { onNavigate(destination) },
                                        colors =
                                        ButtonDefaults.textButtonColors(
                                            contentColor =
                                            if (isSelected) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                        ),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        ) {
                                            Icon(
                                                imageVector = destination.icon,
                                                contentDescription = destination.title,
                                                modifier = Modifier.size(18.dp),
                                            )
                                            Text(
                                                text = destination.title,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight =
                                                if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                fontSize = 14.sp,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // COMPACT (PHONE) / MEDIUM (TABLET): Standard Top Bar
                    TopAppBar(
                        title = {
                            Text(
                                text = currentScreen.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                            )
                        },
                        navigationIcon = {
                            if (!currentScreen.isTopLevel) {
                                IconButton(onClick = onBackClick) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "Back",
                                    )
                                }
                            } else {
                                IconButton(onClick = onOpenDrawer) {
                                    Icon(
                                        imageVector = Icons.Default.Menu,
                                        contentDescription = "Open Menu",
                                    )
                                }
                            }
                        },
                        colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}
