package com.smach.zapmancer.features.projects.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.components.EmptyState
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.theme.Warning
import com.smach.zapmancer.features.projects.state.ProjectListUiState
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
                AutoTypingSearchBarEmptyState(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    onSearchBarClick = { viewModel.onEvent(ProjectListEvent.SearchClicked) }
                )
                CategoryFilterBar(
                    selectedCategory = state.category,
                    onCategoryChange = { viewModel.onEvent(ProjectListEvent.CategorySelected(it)) },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        ProjectListContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}


@Composable
fun ProjectListContent(
    modifier: Modifier = Modifier,
    state: ProjectListUiState,
    onEvent: (ProjectListEvent) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        if (state.isLoading) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                )
            }
        }
        if (state.error != null) {
            item {
                EmptyState(
                    title = state.error,
                    description = "No projects found",
                    buttonText = "Refresh",
                    onButtonClick = { onEvent(ProjectListEvent.Refresh) },
                    icon = Icons.Default.Search
                )
            }
        }

        val projects = if (state.category == ProjectCategory.ALL) {
            state.projects
        } else {
            state.projects.filter {
                it.category == state.category
            }
        }
//
//        item {
//            Spacer(modifier = Modifier.height(8.dp))
//            PortfolioHeader()
//        }

        if (projects.isEmpty() && !state.isLoading && state.error == null) {
            item {
                EmptyState(
                    title = "No projects found",
                    description = "There are no projects available in the ${state.category.displayName} category.",
                    icon = Icons.Outlined.Devices,
                )
            }
        }

        items(projects, key = { it.id }) { project ->
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                visible = true
            }
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = tween(400)) + slideInVertically(
                    initialOffsetY = { it / 4 },
                    animationSpec = tween(400)
                ),
                exit = fadeOut()
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
            val tintColor = when (category) {
                ProjectCategory.ALL -> MaterialTheme.colorScheme.primary
                ProjectCategory.DESIGN -> MaterialTheme.colorScheme.tertiary
                ProjectCategory.DEVELOPMENT -> MaterialTheme.colorScheme.primary
                ProjectCategory.MARKETING -> MaterialTheme.colorScheme.secondary
            }
            FilterChip(
                selected = isSelected,
                onClick = { onCategoryChange(category) },
                label = {
                    Text(
                        category.displayName,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = tintColor.copy(alpha = 0.15f),
                    selectedLabelColor = tintColor,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isSelected) tintColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(20.dp),
            )
        }
    }
}

@Composable
fun PortfolioHeader() {
    Column {
        Text(
            "Get Projects ",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Spacer(modifier = Modifier.height(8.dp))
        DashedDivider()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Manage and track your active development and design cycles.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
fun DashedDivider() {
    val drawLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    Canvas(
        Modifier
            .fillMaxWidth()
            .height(1.dp),
    ) {
        drawLine(
            color = drawLineColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProjectItemCard(project: ProjectUiModel, onClick: () -> Unit = {}) {
    val icon = when (project.category) {
        ProjectCategory.DESIGN -> Icons.Outlined.Palette
        ProjectCategory.DEVELOPMENT -> Icons.Outlined.Devices
        ProjectCategory.MARKETING -> Icons.Outlined.Campaign
        ProjectCategory.ALL -> Icons.Outlined.Devices
    }

    val accentColor = when (project.category) {
        ProjectCategory.DESIGN -> MaterialTheme.colorScheme.tertiary
        ProjectCategory.DEVELOPMENT -> MaterialTheme.colorScheme.primary
        ProjectCategory.MARKETING -> MaterialTheme.colorScheme.secondary
        ProjectCategory.ALL -> MaterialTheme.colorScheme.primary
    }

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
                        Text(
                            project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }

                Surface(
                    color = project.status.color().copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(project.status.color())
                        )
                        Text(
                            project.status.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = project.status.color(),
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }
                }
            }

//            Spacer(modifier = Modifier.height(12.dp))
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
                        .clip(RoundedCornerShape(3.dp)),
                    color = accentColor,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                )
            }

            if (project.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    project.tags.forEach { tag ->
                        Surface(
                            color = accentColor.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(12.dp),
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
                                    color = labelColors[index % labelColors.size]
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
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
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

    var displayedText by remember { mutableStateOf("") }
    var cursorVisible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        while (true) {
            cursorVisible = !cursorVisible
            delay(500.milliseconds)
        }
    }

    LaunchedEffect(hints) {
        var hintIndex = 0
        while (true) {
            val currentHint = hints[hintIndex]

            for (i in 0..currentHint.length) {
                displayedText = currentHint.substring(0, i)
                delay(2000.milliseconds) // Typing speed (adjust for faster/slower)
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
        shape = RoundedCornerShape(28.dp),
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


data class ProjectUiModel(
    val id: String,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val footerIcon: ImageVector? = null,
    val membersCount: Int = 0,
)

@Composable
fun ProjectStatus.color(): Color = when (this) {
    ProjectStatus.ACTIVE -> MaterialTheme.colorScheme.primary
    ProjectStatus.PENDING -> Warning
    ProjectStatus.DONE -> MaterialTheme.colorScheme.outline
}

object ProjectPreviewData {

    val projects = listOf(
        ProjectUiModel(
            id = "1",
            category = ProjectCategory.DEVELOPMENT,
            status = ProjectStatus.ACTIVE,
            title = "Neural Engine Alpha",
            description = "High-performance inference engine...",
            progress = 78,
            footerText = "Optimization Progress",
            membersCount = 2,
        ),

        ProjectUiModel(
            id = "2",
            category = ProjectCategory.DESIGN,
            status = ProjectStatus.PENDING,
            title = "Lumina Design System",
            description = "Unified token-based architecture...",
            showImagePlaceholder = true,
            footerText = "Review: Oct 24",
        ),
    )
}

@Preview
@Composable
fun ProjectListScreenPreview() {
    MaterialTheme {
        ProjectListContent(
            state = ProjectListUiState(),
            onEvent = {},
        )
    }
}

