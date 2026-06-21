package com.smach.zapmancer.features.home.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.home.state.RecentActivity
import com.smach.zapmancer.features.home.viewmodel.HomeEffect
import com.smach.zapmancer.features.home.viewmodel.HomeEvent
import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

val ZapGold = Color(0xFFFFD700)

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToPostProject: () -> Unit = {},
    onNavigateToClientProposals: (String) -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToSubmitProposal: () -> Unit = {},
    showSnackbar: (String) -> Unit = {}
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowToast -> {
                    showSnackbar(effect.message)
                }
            }
        }
    }

    HomeScreen(
        state = state,
        onCreateProjectClick = {
            if (state.isClientMode) onNavigateToPostProject() else onNavigateToProjects()
        },
        onNavigateToSettings = onNavigateToSettings,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToProposal = {
            if (state.isClientMode) onNavigateToClientProposals("1") else onNavigateToSubmitProposal()
        },
        onExportCsvClick = { viewModel.onEvent(HomeEvent.ExportCsv) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onCreateProjectClick: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToProposal: () -> Unit = {},
    onExportCsvClick: () -> Unit = {}
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Zapmancer",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                MaterialTheme.colorScheme.surfaceVariant,
                                MaterialTheme.shapes.medium
                            )
                            .clickable {
                                scope.launch { drawerState.close() }
                                onNavigateToProfile()
                            }
                            .padding(12.dp)
                    ) {
                        UserAvatar(
                            imageUrl = null,
                            size = 40.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "Alex Rivera",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "Senior Developer",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        DrawerItem(
                            icon = Icons.Default.Work,
                            label = "Home Dashboard",
                            onClick = {
                                scope.launch { drawerState.close() }
                            }
                        )
                        if (state.isClientMode) {
                            DrawerItem(
                                icon = Icons.Default.Payments,
                                label = "Post a Project",
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onCreateProjectClick()
                                }
                            )
                            DrawerItem(
                                icon = Icons.Default.Star,
                                label = "Review Project Bids",
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onNavigateToProposal()
                                }
                            )
                        } else {
                            DrawerItem(
                                icon = Icons.Default.Payments,
                                label = "Create Proposal",
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onNavigateToProposal()
                                }
                            )
                            DrawerItem(
                                icon = Icons.Default.Star,
                                label = "My Profile",
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    onNavigateToProfile()
                                }
                            )
                        }
                        DrawerItem(
                            icon = Icons.Default.Settings,
                            label = "Settings",
                            onClick = {
                                scope.launch { drawerState.close() }
                                onNavigateToSettings()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "Version 1.0.0",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                ZapmancerTopBar(
                    titleContent = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                "Zapmancer",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold)
                            )
                        }
                    },
                    showMenuButton = true,
                    onMenuClick = {
                        scope.launch { drawerState.open() }
                    },
                    actions = {
                        IconButton(onClick = {}) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        UserAvatar(
                            imageUrl = null,
                            size = 42.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            borderWidth = 1.dp,
                            borderColor = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    drawBottomBorder = true
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            HomeContent(
                state = state,
                onCreateProjectClick = onCreateProjectClick,
                onExportCsvClick = onExportCsvClick,
                modifier = Modifier.padding(padding)
            )
        }
    }
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onCreateProjectClick: () -> Unit,
    onExportCsvClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "Welcome back, ${state.userName}".uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Start your Journey.",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (state.isClientMode) {
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Total Spent",
                    value = "$14,800.00",
                    accentColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Payments,
                    growth = "+8.2%"
                )
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Active Job Posts",
                    value = "3",
                    accentColor = ZapGold,
                    icon = Icons.Default.Work,
                    secondaryValue = "/ 5 capacity"
                )
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Proposals Received",
                    value = "12",
                    accentColor = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Star,
                    secondaryValue = "avg 4 bids/post"
                )
            } else {
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Total Earnings",
                    value = state.totalEarnings,
                    accentColor = MaterialTheme.colorScheme.primary,
                    icon = Icons.Default.Payments,
                    growth = state.earningsGrowth
                )
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Current Projects",
                    value = state.activeProjectsCount.toString(),
                    accentColor = ZapGold,
                    icon = Icons.Default.Work,
                    secondaryValue = "/ ${state.totalCapacity} capacity"
                )
                StatCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "System Rating",
                    value = state.systemRating.toString(),
                    accentColor = MaterialTheme.colorScheme.secondary,
                    icon = Icons.Default.Star,
                    isRating = true
                )
            }
        }
        Button(
            onClick = onCreateProjectClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            shape = MaterialTheme.shapes.large,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text(
                text = if (state.isClientMode) "Post a New Project" else "Browse Projects",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = onExportCsvClick) {
                    Text(
                        "Export CSV",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                state.recentActivities.forEach { activity ->
                    ActivityRow(
                        activity = activity,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
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
    isRating: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                accentColor.copy(alpha = 0.2f),
                                MaterialTheme.shapes.extraSmall
                            )
                            .border(
                                1.dp,
                                accentColor.copy(alpha = 0.4f),
                                MaterialTheme.shapes.extraSmall
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (growth != null) {
                        Surface(
                            color = accentColor,
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                growth,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }

                Column {
                    Text(
                        title.uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            value,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (secondaryValue != null) {
                            Text(
                                secondaryValue,
                                modifier = Modifier.padding(bottom = 8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isRating) {
                            Row(modifier = Modifier.padding(bottom = 12.dp)) {
                                repeat(5) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = ZapGold,
                                        modifier = Modifier.size(18.dp)
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
    activity: RecentActivity,
    modifier: Modifier = Modifier
) {
    val accentColor = when (activity.status) {
        ActivityStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        ActivityStatus.REVIEWING -> ZapGold
        ActivityStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        ActivityStatus.CRITICAL -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier
                .drawAccentLine(accentColor)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                accentColor.copy(alpha = 0.15f),
                                MaterialTheme.shapes.small
                            )
                            .border(
                                1.dp,
                                accentColor.copy(alpha = 0.4f),
                                MaterialTheme.shapes.small
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activity.categoryTag,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = accentColor
                        )
                    }

                    Column {
                        Text(
                            text = activity.projectName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = activity.category,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = activity.value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    color = accentColor,
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = activity.status.name
                            .replace('_', ' '),
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}


@Preview(
    device = PIXEL_9_PRO
)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            state = HomeUiState(
                userName = "Alex",
                recentActivities = listOf(
                    RecentActivity(
                        "1",
                        "Neural Engine Optimizer",
                        "Infrastructure",
                        "AI",
                        ActivityStatus.IN_PROGRESS,
                        "2h ago",
                        "$12,400.00"
                    ),
                    RecentActivity(
                        "2",
                        "Dashboard Redesign",
                        "Visual Design",
                        "UX",
                        ActivityStatus.REVIEWING,
                        "Yesterday",
                        "$4,200.00"
                    ),
                    RecentActivity(
                        "3",
                        "SQL Latency Patch",
                        "Backend",
                        "DB",
                        ActivityStatus.COMPLETED,
                        "Oct 24",
                        "$8,150.00"
                    ),
                    RecentActivity(
                        "4",
                        "Security Audit",
                        "Compliance",
                        "SY",
                        ActivityStatus.CRITICAL,
                        "Oct 22",
                        "$15,000.00"
                    )
                )
            )
        )
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
