package com.smach.zapmancer.features.projects.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.components.CategoryFilterChip
import com.smach.zapmancer.features.common.components.EmptyState
import com.smach.zapmancer.features.common.components.ErrorState
import com.smach.zapmancer.features.common.components.ProjectStatusBadge
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.components.accentColor
import com.smach.zapmancer.features.common.components.icon
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import com.smach.zapmancer.features.projects.state.ProjectUiModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectListEffect
import com.smach.zapmancer.features.projects.viewmodel.ProjectListEvent
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    viewModel: ProjectListViewModel = koinViewModel(),
    onNavigateToProjectDetail: (String) -> Unit = {},
    onNavigateToSearch: () -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    var isSearchBarVisible by remember { mutableStateOf(true) }
    var lastScrollIndex by remember { mutableStateOf(0) }
    var lastScrollOffset by remember { mutableStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
            .collect { (currentIndex, currentOffset) ->
                if (currentIndex != lastScrollIndex || kotlin.math.abs(currentOffset - lastScrollOffset) > 15) {
                    val isScrollingDown = when {
                        currentIndex > lastScrollIndex -> true
                        currentIndex < lastScrollIndex -> false
                        else -> currentOffset > lastScrollOffset
                    }

                    if (isScrollingDown && (currentIndex > 0 || currentOffset > 150)) {
                        isSearchBarVisible = false
                    } else if (!isScrollingDown) {
                        isSearchBarVisible = true
                    }

                    lastScrollIndex = currentIndex
                    lastScrollOffset = currentOffset
                }
            }
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProjectListEffect.NavigateToProjectDetail -> {
                    onNavigateToProjectDetail(effect.projectId)
                }

                ProjectListEffect.NavigateToSearch -> {
                    onNavigateToSearch()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                ZapmancerTopBar(
                    title = "Zapmancer",
                    containerColor = MaterialTheme.colorScheme.background,
                    drawBottomBorder = false,
                )
                AnimatedVisibility(
                    visible = isSearchBarVisible,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut(),
                ) {
                    Column {
                        AutoTypingSearchBarEmptyState(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            onSearchBarClick = { viewModel.onEvent(ProjectListEvent.SearchClicked) },
                        )
                        CategoryFilterBar(
                            selectedCategory = state.category,
                            onCategoryChange = {
                                viewModel.onEvent(
                                    ProjectListEvent.CategorySelected(
                                        it,
                                    ),
                                )
                            },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        )
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        ProjectListContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEvent = viewModel::onEvent,
            listState = listState,
        )
    }
}

@Composable
fun ProjectListContent(
    modifier: Modifier = Modifier,
    state: ProjectListUiState,
    onEvent: (ProjectListEvent) -> Unit,
    listState: LazyListState = rememberLazyListState(),
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
            )
        } else if (state.error != null) {
            ErrorState(
                title = state.error,
                description = "Failed to load projects.",
                buttonText = "Refresh",
                onButtonClick = { onEvent(ProjectListEvent.Refresh) },
            )
        } else {
            val projects = if (state.category == ProjectCategory.ALL) {
                state.projects
            } else {
                state.projects.filter {
                    it.category == state.category
                }
            }

            if (projects.isEmpty()) {
                EmptyState(
                    title = "No projects found",
                    description = "There are no projects available in the ${state.category.displayName} category.",
                    icon = Icons.Outlined.Devices,
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    items(projects, key = { it.id }) { project ->
                        val isPreview = LocalInspectionMode.current
                        var visible by remember { mutableStateOf(isPreview) }
                        LaunchedEffect(Unit) {
                            if (!isPreview) {
                                visible = true
                            }
                        }
                        AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
                                initialOffsetY = { it / 4 },
                                animationSpec = tween(400),
                            ),
                            exit = fadeOut(),
                        ) {
                            ProjectItemCard(
                                project = project,
                                onClick = { onEvent(ProjectListEvent.ProjectClicked(project.id)) },
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryFilterBar(
    selectedCategory: ProjectCategory,
    onCategoryChange: (ProjectCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        items(ProjectCategory.entries.toList()) { category ->
            val isSelected = selectedCategory == category
            CategoryFilterChip(
                category = category,
                isSelected = isSelected,
                onClick = { onCategoryChange(category) },
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectItemCard(project: ProjectUiModel, onClick: () -> Unit = {}) {
    val icon = project.category.icon()
    val accentColor = project.category.accentColor(MaterialTheme.colorScheme)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)),
    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(accentColor.copy(alpha = 0.08f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = project.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp,
                        )
                    }
                }

                ProjectStatusBadge(status = project.status)
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                project.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis,
            )

            if (project.progress != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        "Optimization Progress",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        "${project.progress}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { project.progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    color = accentColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                )
            }

            if (project.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    project.tags.forEach { tag ->
                        Surface(
                            color = accentColor.copy(alpha = 0.08f),
                            shape = MaterialTheme.shapes.medium,
                            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.15f)),
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = accentColor,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            }

            if (project.showImagePlaceholder) {
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        accentColor.copy(alpha = 0.2f),
                                        Color.Transparent,
                                    ),
                                    center = Offset(400f, 200f),
                                    radius = 300f,
                                ),
                            ),
                    )
                }
            }

            if (project.membersCount > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val avatarTints = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.secondaryContainer,
                    )
                    val labelColors = listOf(
                        MaterialTheme.colorScheme.onPrimaryContainer,
                        MaterialTheme.colorScheme.onSecondaryContainer,
                    )
                    val displayAvatars = minOf(project.membersCount, 2)
                    repeat(displayAvatars) { index ->
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            shape = CircleShape,
                            color = avatarTints[index % avatarTints.size],
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (index == 0) "A" else "B",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = labelColors[index % labelColors.size],
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width((-8).dp))
                    }
                    if (project.membersCount > 2) {
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "+${project.membersCount - 2}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (project.footerIcon != null) {
                        Icon(
                            project.footerIcon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(16.dp),
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        project.footerText ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium,
                    )
                }

                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

@Composable
fun AutoTypingSearchBarEmptyState(
    modifier: Modifier = Modifier,
    onSearchBarClick: () -> Unit,
) {
    val hints = listOf(
        "Find Ktor backend gigs...",
        "Hire a mobile UI designer...",
        "Explore freelance projects...",
    )

    val isPreview = LocalInspectionMode.current
    var displayedText by remember { mutableStateOf(if (isPreview) hints.first() else "") }
    var cursorVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isPreview) return@LaunchedEffect
        while (true) {
            cursorVisible = !cursorVisible
            delay(500.milliseconds)
        }
    }

    LaunchedEffect(hints) {
        if (isPreview) return@LaunchedEffect
        var hintIndex = 0
        while (true) {
            val currentHint = hints[hintIndex]

            for (i in 0..currentHint.length) {
                displayedText = currentHint.substring(0, i)
                delay(150.milliseconds) // Typing speed (adjust for faster/slower)
            }

            delay(2000.milliseconds)

            for (i in currentHint.length downTo 0) {
                displayedText = currentHint.substring(0, i)
                delay(30.milliseconds) // Erasing speed (usually faster than typing)
            }

            delay(500.milliseconds)

            hintIndex = (hintIndex + 1) % hints.size
        }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onSearchBarClick() },
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = displayedText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Text(
                text = "|",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.alpha(if (cursorVisible) 1f else 0f),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewScreenSizes
@Composable
fun ProjectListScreenPreview() {
    val sampleProjects = listOf(
        ProjectUiModel(
            id = "1",
            category = ProjectCategory.DEVELOPMENT,
            status = ProjectStatus.ACTIVE,
            title = "Neural Engine Alpha",
            description = "High-performance inference engine for edge devices.",
            progress = 78,
            footerText = "Optimization Progress",
            membersCount = 2,
        ),
        ProjectUiModel(
            id = "2",
            category = ProjectCategory.DESIGN,
            status = ProjectStatus.PENDING,
            title = "Lumina Design System",
            description = "Unified token-based architecture for multiplatform apps.",
            showImagePlaceholder = true,
            footerText = "Review: Oct 24",
        ),
        ProjectUiModel(
            id = "3",
            category = ProjectCategory.WRITING,
            status = ProjectStatus.ACTIVE,
            title = "Whitepaper & Technical Docs",
            description = "Drafting comprehensive technical documentation and whitepaper for Zapmancer Protocol.",
            progress = 45,
            footerText = "Milestone 2/4",
            membersCount = 1,
        ),
    )

    AppTheme {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    ZapmancerTopBar(
                        title = "Zapmancer",
                        containerColor = MaterialTheme.colorScheme.background,
                        drawBottomBorder = false,
                    )
                    AutoTypingSearchBarEmptyState(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        onSearchBarClick = {},
                    )
                    CategoryFilterBar(
                        selectedCategory = ProjectCategory.ALL,
                        onCategoryChange = {},
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    )
                }
            },
            containerColor = MaterialTheme.colorScheme.background,
        ) { paddingValues ->
            ProjectListContent(
                modifier = Modifier.padding(paddingValues),
                state = ProjectListUiState(
                    projects = sampleProjects,
                ),
                onEvent = {},
            )
        }
    }
}
