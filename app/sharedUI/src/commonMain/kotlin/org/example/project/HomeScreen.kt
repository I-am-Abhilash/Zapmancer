package com.smach.zapmancer.presentation.home.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowSizeClass
import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.UserActivity
import com.smach.zapmancer.presentation.alerts.screen.drawAccentLine
import com.smach.zapmancer.presentation.common.adaptive.isExpandedWidth
import com.smach.zapmancer.presentation.common.adaptive.isMediumWidth
import com.smach.zapmancer.presentation.common.components.AppShimmer
import com.smach.zapmancer.presentation.common.components.EmptyState
import com.smach.zapmancer.presentation.common.components.UserAvatar
import com.smach.zapmancer.presentation.common.components.ZapmancerTopBar
import com.smach.zapmancer.presentation.common.theme.pill
import com.smach.zapmancer.presentation.home.state.HomeUiState
import com.smach.zapmancer.presentation.home.viewmodel.HomeEffect
import com.smach.zapmancer.presentation.home.viewmodel.HomeEvent
import com.smach.zapmancer.presentation.home.viewmodel.HomeViewModel
import kotlinx.datetime.TimeZone.Companion.currentSystemDefault
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Clock
import kotlin.time.Instant.Companion.fromEpochMilliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onCreateProjectClick: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onCompleteProfileClick: () -> Unit = {},
    showSnackbar: (String) -> Unit = {},
    onSearchClick: () -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is HomeEffect.ShowToast -> {
                    showSnackbar(effect.message)
                }

                HomeEffect.NavigateToProfile -> onNavigateToProfile()

                HomeEffect.NavigateToCompleteProfile -> onCompleteProfileClick()

                HomeEffect.NavigateToSearch -> onSearchClick()

                HomeEffect.NavigateToCreateProject -> onCreateProjectClick()
            }
        }
    }

    HomeContent(
        state = state,
        onEvent = viewModel::onEvent,
        windowSizeClass = windowSizeClass,
    )
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onEvent: (HomeEvent) -> Unit,
    windowSizeClass: WindowSizeClass = currentWindowAdaptiveInfo().windowSizeClass,
) {
    Scaffold(
        topBar = {
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
                actions = {
                    UserAvatar(
                        onClick = { onEvent(HomeEvent.ProfileClicked) },
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
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        if (state.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AppShimmer(modifier = Modifier.width(120.dp).height(16.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    AppShimmer(modifier = Modifier.fillMaxWidth(0.6f).height(32.dp))
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    repeat(3) {
                        AppShimmer(modifier = Modifier.fillMaxWidth().height(160.dp))
                    }
                }
            }
        } else {
            when {
                windowSizeClass.isExpandedWidth -> {
                    // EXPANDED / DESKTOP / WEB VIEW (≥840dp): 2-pane Dashboard Layout
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        // Left Pane: Welcome, Stat Row, Primary CTA & Profile Banner
                        Column(
                            modifier = Modifier
                                .weight(1.2f)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(24.dp),
                        ) {
                            HomeWelcomeHeader(userName = state.userName)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                            ) {
                                StatCardsGroup(
                                    state = state,
                                    itemModifier = Modifier.weight(1f).height(160.dp),
                                )
                            }

                            PrimaryActionButton(
                                isClientMode = state.isClientMode,
                                onClick = { onEvent(HomeEvent.CreateProjectClicked) },
                            )

                            if (state.showCompleteProfileBanner) {
                                CompleteProfileBanner(onCompleteProfileClick = { onEvent(HomeEvent.CompleteProfileClicked) })
                            }
                        }

                        // Right Pane: Activity Stream Card
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                                .verticalScroll(rememberScrollState()),
                        ) {
                            RecentActivitySection(
                                recentActivities = state.recentActivities,
                                onEvent = onEvent,
                            )
                        }
                    }
                }

                windowSizeClass.isMediumWidth -> {
                    // MEDIUM VIEW (600dp–839dp): 3-Card Horizontal Stat Row + Stacked Content
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 24.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        HomeWelcomeHeader(userName = state.userName)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            StatCardsGroup(
                                state = state,
                                itemModifier = Modifier.weight(1f).height(160.dp),
                            )
                        }

                        PrimaryActionButton(
                            isClientMode = state.isClientMode,
                            onClick = { onEvent(HomeEvent.CreateProjectClicked) },
                        )

                        if (state.showCompleteProfileBanner) {
                            CompleteProfileBanner(onCompleteProfileClick = { onEvent(HomeEvent.CompleteProfileClicked) })
                        }

                        RecentActivitySection(
                            recentActivities = state.recentActivities,
                            onEvent = onEvent,
                        )
                    }
                }

                else -> {
                    // COMPACT VIEW (<600dp): Mobile 1-Column Stacked Layout
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp),
                    ) {
                        HomeWelcomeHeader(userName = state.userName)

                        if (state.showCompleteProfileBanner) {
                            CompleteProfileBanner(onCompleteProfileClick = { onEvent(HomeEvent.CompleteProfileClicked) })
                        }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            StatCardsGroup(
                                state = state,
                                itemModifier = Modifier.fillMaxWidth().height(160.dp),
                            )
                        }

                        PrimaryActionButton(
                            isClientMode = state.isClientMode,
                            onClick = { onEvent(HomeEvent.CreateProjectClicked) },
                        )

                        RecentActivitySection(
                            recentActivities = state.recentActivities,
                            onEvent = onEvent,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeWelcomeHeader(userName: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Welcome back, $userName".uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
            ),
            color = MaterialTheme.colorScheme.primary,
        )
        Text(
            text = "Start your Journey.",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun StatCardsGroup(
    state: HomeUiState,
    itemModifier: Modifier,
) {
    if (state.isClientMode) {
        StatCard(
            modifier = itemModifier,
            title = "Total Spent",
            value = state.totalSpent,
            accentColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Default.Payments,
            growth = state.spentGrowth.ifEmpty { null },
        )
        StatCard(
            modifier = itemModifier,
            title = "Active Job Posts",
            value = state.activeJobPostsCount.toString(),
            accentColor = MaterialTheme.colorScheme.tertiary,
            icon = Icons.Default.Work,
            secondaryValue = "/ ${state.totalCapacity} capacity",
        )
        StatCard(
            modifier = itemModifier,
            title = "Proposals Received",
            value = state.proposalsReceivedCount.toString(),
            accentColor = MaterialTheme.colorScheme.secondary,
            icon = Icons.Default.Star,
        )
    } else {
        StatCard(
            modifier = itemModifier,
            title = "Total Earnings",
            value = state.totalEarnings,
            accentColor = MaterialTheme.colorScheme.primary,
            icon = Icons.Default.Payments,
            growth = state.earningsGrowth,
        )
        StatCard(
            modifier = itemModifier,
            title = "Current Projects",
            value = state.activeProjectsCount.toString(),
            accentColor = MaterialTheme.colorScheme.tertiary,
            icon = Icons.Default.Work,
            secondaryValue = "/ ${state.totalCapacity} capacity",
        )
        StatCard(
            modifier = itemModifier,
            title = "System Rating",
            value = state.systemRating.toString(),
            accentColor = MaterialTheme.colorScheme.secondary,
            icon = Icons.Default.Star,
            isRating = true,
        )
    }
}

@Composable
private fun PrimaryActionButton(
    isClientMode: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        shape = MaterialTheme.shapes.pill,
    ) {
        Text(
            text = if (isClientMode) "Post a New Project" else "Browse Projects",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Composable
private fun RecentActivitySection(
    recentActivities: List<UserActivity>,
    onEvent: (HomeEvent) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            IconButton(onClick = { onEvent(HomeEvent.ExportCsv) }) {
                Icon(
                    Icons.Default.Print,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (recentActivities.isEmpty()) {
                EmptyState(
                    title = "All caught up!",
                    description = "You have no activity yet start a project or post one.",
                    icon = Icons.Outlined.Search,
                    buttonText = "Refresh",
                )
            } else {
                recentActivities.forEach { activity ->
                    ActivityRow(
                        activity = activity,
                        modifier = Modifier.fillMaxWidth(),
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
    isRating: Boolean = false,
) {
    val cardHeight = 160.dp
    Card(
        modifier = modifier.height(cardHeight),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                accentColor.copy(alpha = 0.2f),
                                MaterialTheme.shapes.extraSmall,
                            )
                            .border(
                                1.dp,
                                accentColor.copy(alpha = 0.4f),
                                MaterialTheme.shapes.extraSmall,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    if (growth != null) {
                        Surface(color = accentColor, shape = MaterialTheme.shapes.small) {
                            Text(
                                growth,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
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
                            Row(
                                modifier = Modifier
                                    .padding(bottom = 12.dp)
                                    .semantics { contentDescription = "Rating: $value out of 5" },
                            ) {
                                repeat(5) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
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
        ActivityStatus.REVIEWING -> MaterialTheme.colorScheme.secondary
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
                        color = MaterialTheme.colorScheme.onPrimary,
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
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(accentColor.copy(alpha = 0.15f), MaterialTheme.shapes.small)
                            .border(
                                1.dp,
                                accentColor.copy(alpha = 0.4f),
                                MaterialTheme.shapes.small,
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = activity.category.displayName.take(1),
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
                            text = activity.category.displayName,
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
                    text = formatTimestamp(activity.timestamp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun CompleteProfileBanner(
    onCompleteProfileClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .drawAccentLine(MaterialTheme.colorScheme.primary),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Complete Your Profile",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Complete your freelancer profile to unlock your full potential and get hired by clients.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = onCompleteProfileClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.align(Alignment.End),
            ) {
                Text(
                    text = "Complete Profile",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val now = Clock.System.now().toEpochMilliseconds()
    MaterialTheme {
        HomeContent(
            state = HomeUiState(
                userName = "Alex",
                recentActivities = listOf(
                    UserActivity(
                        "1",
                        "Neural Engine Optimizer",
                        ProjectCategory.DEVELOPMENT,
                        "AI",
                        ActivityStatus.IN_PROGRESS,
                        now - 2 * 3600 * 1000L,
                        "$12,400.00",
                    ),
                    UserActivity(
                        "2",
                        "Dashboard Redesign",
                        ProjectCategory.DESIGN,
                        "UX",
                        ActivityStatus.REVIEWING,
                        now - 24 * 3600 * 1000L,
                        "$4,200.00",
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val now = Clock.System.now().toEpochMilliseconds()
    val diffMs = now - timestamp
    if (diffMs < 0) return "Just now"
    val diffSec = diffMs / 1000
    if (diffSec < 60) return "Just now"
    val diffMin = diffSec / 60
    if (diffMin < 60) return "${diffMin}m ago"
    val diffHours = diffMin / 60
    if (diffHours < 24) return "${diffHours}h ago"
    val diffDays = diffHours / 24
    if (diffDays < 7) return "${diffDays}d ago"
    val instant = fromEpochMilliseconds(timestamp)
    val localDateTime = instant.toLocalDateTime(currentSystemDefault())
    val monthName = localDateTime.month.name.take(3).lowercase()
        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    return "$monthName ${localDateTime.dayOfMonth}"
}
