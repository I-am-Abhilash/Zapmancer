package com.smach.zapmancer.features.home.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.adaptive.AdaptiveScaffold
import com.smach.zapmancer.features.common.adaptive.LocalWindowLayout
import com.smach.zapmancer.features.common.adaptive.NavDestination
import com.smach.zapmancer.features.common.adaptive.WindowLayout
import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
import com.smach.zapmancer.features.common.components.LocalDrawerController
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.home.viewmodel.HomeEffect
import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
import org.koin.compose.viewmodel.koinViewModel

val ZapGold = Color(0xFFFFD700)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onCreateProjectClick: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onExportCsvClick: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    currentRoute: String = NavDestination.Home.route,
    showSnackbar: (String) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val drawerController = LocalDrawerController.current
    val windowLayout = rememberWindowLayout()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowToast -> {
                    showSnackbar(effect.message)
                }
            }
        }
    }

    CompositionLocalProvider(LocalWindowLayout provides windowLayout) {
        AdaptiveScaffold(
            currentRoute = currentRoute,
            onNavigate = { dest ->
                when (dest) {
                    NavDestination.Home -> Unit
                    NavDestination.Projects -> onNavigateToProjects()
                    NavDestination.Messages -> onNavigateToMessages()
                    NavDestination.Notifications -> onNavigateToNotifications()
                    NavDestination.Profile -> onNavigateToProfile()
                }
            },
            onCreateProjectClick = onCreateProjectClick,
            title = {
                ZapmancerTopBar(
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Text(
                                "Zapmancer",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            )
                        }
                    },
                    showMenuButton = windowLayout.isCompact,
                    onMenuClick = { drawerController.open() },
                    actions = {
                        UserAvatar(
                            onClick = onNavigateToProfile,
                            imageUrl = null,
                            size = 32.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            borderWidth = 1.dp,
                            borderColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 4.dp),
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    drawBottomBorder = true,
                )
            },
        ) { padding ->
            HomeContent(
                paddingValues = padding,
                state = state,
                onCreateProjectClick = onCreateProjectClick,
                onExportCsvClick = onExportCsvClick,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeContent(
    paddingValues: PaddingValues,
    state: HomeUiState,
    onCreateProjectClick: () -> Unit,
    onExportCsvClick: () -> Unit,
) {
    val windowLayout = LocalWindowLayout.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = windowLayout.contentMaxWidthDp.dp)
                .padding(horizontal = windowLayout.screenHorizontalPaddingDp.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Column {
                        Text(
                            "Welcome back, ${state.userName}".uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp,
                            ),
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Text(
                            "Start your Journey.",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    val cardHeight = when (windowLayout) {
                        WindowLayout.Compact -> 160.dp
                        WindowLayout.Medium -> 180.dp
                        WindowLayout.Expanded -> 200.dp
                    }
                    val cardModifier = when (windowLayout) {
                        WindowLayout.Compact -> Modifier.fillMaxWidth()
                        WindowLayout.Medium -> Modifier.weight(1f).height(cardHeight)
                        WindowLayout.Expanded -> Modifier.weight(1f).height(cardHeight).widthIn(min = 280.dp)
                    }
                    if (state.isClientMode) {
                        StatCard(
                            modifier = cardModifier,
                            title = "Total Spent",
                            value = "$14,800.00",
                            accentColor = MaterialTheme.colorScheme.primary,
                            icon = Icons.Default.Payments,
                            growth = "+8.2%",
                        )
                        StatCard(
                            modifier = cardModifier,
                            title = "Active Job Posts",
                            value = "3",
                            accentColor = ZapGold,
                            icon = Icons.Default.Work,
                            secondaryValue = "/ 5 capacity",
                        )
                        StatCard(
                            modifier = cardModifier,
                            title = "Proposals Received",
                            value = "12",
                            accentColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Default.Star,
                            secondaryValue = "avg 4 bids/post",
                        )
                    } else {
                        StatCard(
                            modifier = cardModifier,
                            title = "Total Earnings",
                            value = state.totalEarnings,
                            accentColor = MaterialTheme.colorScheme.primary,
                            icon = Icons.Default.Payments,
                            growth = state.earningsGrowth,
                        )
                        StatCard(
                            modifier = cardModifier,
                            title = "Current Projects",
                            value = state.activeProjectsCount.toString(),
                            accentColor = ZapGold,
                            icon = Icons.Default.Work,
                            secondaryValue = "/ ${state.totalCapacity} capacity",
                        )
                        StatCard(
                            modifier = cardModifier,
                            title = "System Rating",
                            value = state.systemRating.toString(),
                            accentColor = MaterialTheme.colorScheme.secondary,
                            icon = Icons.Default.Star,
                            isRating = true,
                        )
                    }
                }
                Button(
                    onClick = onCreateProjectClick,
                    modifier = when (windowLayout) {
                        WindowLayout.Compact -> Modifier.fillMaxWidth()
                        WindowLayout.Medium -> Modifier.width(300.dp)
                        WindowLayout.Expanded -> Modifier.width(360.dp)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = MaterialTheme.shapes.large,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    contentPadding = PaddingValues(vertical = 12.dp),
                ) {
                    Text(
                        text = if (state.isClientMode) "Post a New Project" else "Browse Projects",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            "Recent Activity",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                        )
                        TextButton(onClick = onExportCsvClick) {
                            Text(
                                "Export CSV",
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    when (windowLayout) {
                        WindowLayout.Compact -> Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            state.recentActivities.forEach { activity ->
                                ActivityRow(activity = activity, modifier = Modifier.padding(horizontal = 8.dp))
                            }
                        }

                        WindowLayout.Medium -> Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            state.recentActivities.forEach { activity ->
                                ActivityRow(activity = activity, modifier = Modifier.fillMaxWidth())
                            }
                        }

                        WindowLayout.Expanded -> FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            state.recentActivities.forEach { activity ->
                                ActivityRow(
                                    activity = activity,
                                    modifier = Modifier.weight(1f).widthIn(min = 360.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    accentColor: Color,
    icon: ImageVector,
    growth: String? = null,
    secondaryValue: String? = null,
    isRating: Boolean = false,
) {
    val windowLayout = LocalWindowLayout.current
    val cardHeight = when (windowLayout) {
        WindowLayout.Compact -> 160.dp
        else -> 200.dp
    }
    Card(
        modifier = modifier.height(cardHeight),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(accentColor.copy(alpha = 0.2f), MaterialTheme.shapes.extraSmall)
                            .border(1.dp, accentColor.copy(alpha = 0.4f), MaterialTheme.shapes.extraSmall),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                    }

                    if (growth != null) {
                        Surface(color = accentColor, shape = MaterialTheme.shapes.small) {
                            Text(
                                growth,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            )
                        }
                    }
                }

                Column {
                    Text(
                        title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Text(
                            value,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        if (secondaryValue != null) {
                            Text(
                                secondaryValue,
                                modifier = Modifier.padding(bottom = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        if (isRating) {
                            Row(modifier = Modifier.padding(bottom = 12.dp)) {
                                repeat(5) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = ZapGold,
                                        modifier = Modifier.size(18.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRow(
    activity: UserActivity,
    modifier: Modifier = Modifier,
) {
    val accentColor = when (activity.status) {
        ActivityStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        ActivityStatus.REVIEWING -> ZapGold
        ActivityStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        ActivityStatus.CRITICAL -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Surface(color = accentColor, shape = MaterialTheme.shapes.small) {
                    Text(
                        text = activity.status.name.replace('_', ' '),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }
                Text(
                    text = activity.monetaryValue,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(accentColor.copy(alpha = 0.15f), MaterialTheme.shapes.small)
                            .border(1.dp, accentColor.copy(alpha = 0.4f), MaterialTheme.shapes.small),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = activity.category,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = accentColor,
                        )
                    }

                    Column {
                        Text(
                            text = activity.projectName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = activity.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = activity.timestamp,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Preview(device = PIXEL_9_PRO)
@Composable
private fun HomeScreenPreview() {
    AppTheme {
        CompositionLocalProvider(LocalWindowLayout provides WindowLayout.Compact) {
            HomeContent(
                paddingValues = PaddingValues(0.dp),
                state = HomeUiState(
                    userName = "Alex",
                    recentActivities = listOf(
                        UserActivity("1", "Neural Engine Optimizer", "Infrastructure", "AI", ActivityStatus.IN_PROGRESS, "2h ago", "$12,400.00"),
                        UserActivity("2", "Dashboard Redesign", "Visual Design", "UX", ActivityStatus.REVIEWING, "Yesterday", "$4,200.00"),
                        UserActivity("3", "SQL Latency Patch", "Backend", "DB", ActivityStatus.COMPLETED, "Oct 24", "$8,150.00"),
                        UserActivity("4", "Security Audit", "Compliance", "SY", ActivityStatus.CRITICAL, "Oct 22", "$15,000.00"),
                    ),
                ),
                onCreateProjectClick = {},
                onExportCsvClick = {},
            )
        }
    }
}
