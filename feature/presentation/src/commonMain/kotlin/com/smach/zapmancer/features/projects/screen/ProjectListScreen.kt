package com.smach.zapmancer.features.projects.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.theme.Warning
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import com.smach.zapmancer.features.projects.viewmodel.ProjectListEvent
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ProjectListScreen(
    viewModel: ProjectListViewModel = koinViewModel(),
    onEvent: (ProjectListEvent) -> Unit,
    onProjectClick: (Int) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    ProjectListContent(
        state = state,
        onEvent = onEvent,
        onProjectClick = onProjectClick,
        onSearchClick = {},
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListContent(
    state: ProjectListUiState,
    onProjectClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit,
    onEvent: (ProjectListEvent) -> Unit
) {
    val projects = state.projects

    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.background,
                drawBottomBorder = false
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.isLoading) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    )
                }
            }
            if (state.error != null) {
                item {
                    Text(
                        text = state.error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                        textAlign = TextAlign.Center
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


            item {
                Spacer(modifier = Modifier.height(8.dp))
                PortfolioHeader()
            }

            if (projects.isEmpty() && !state.isLoading && state.error == null) {
                item {
                    Text(
                        text = "No projects found for this category.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            items(projects) { project ->
                ProjectItemCard(
                    project = project,
                    onClick = { onProjectClick(project.id) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
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
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        DashedDivider()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Manage and track your active development and design cycles.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            .height(1.dp)
    ) {
        drawLine(
            color = drawLineColor,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}


@Composable
fun ProjectItemCard(project: ProjectUiModel, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
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
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(MaterialTheme.shapes.small)
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = project.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    color = if (project.status == ProjectStatus.ACTIVE) MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.1f
                    ) else accentColor.copy(
                        alpha = 0.1f
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        project.status.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = project.status.color(),
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            if (project.progress != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Optimization Progress",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "${project.progress}%",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { project.progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(MaterialTheme.shapes.extraSmall),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.background,
                )
            }

            if (project.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    project.tags.forEach { tag ->
                        Surface(
                            color = MaterialTheme.colorScheme.background,
                            shape = MaterialTheme.shapes.small,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Medium
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
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    center = Offset(400f, 200f),
                                    radius = 300f
                                )
                            )
                    )
                }
            }

            if (project.membersCount > 0) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(2) {
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {}
                        Spacer(modifier = Modifier.width((-8).dp))
                    }
                    if (project.membersCount > 2) {
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "+${project.membersCount - 2}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (project.footerIcon != null) {
                        Icon(
                            project.footerIcon,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        project.footerText ?: "",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                }

                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

data class ProjectUiModel(
    val id: Int,
    val category: ProjectCategory,
    val status: ProjectStatus,
    val title: String,
    val description: String,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val footerIcon: ImageVector? = null,
    val membersCount: Int = 0
)


@Composable
fun ProjectStatus.color(): Color =
    when (this) {
        ProjectStatus.ACTIVE -> MaterialTheme.colorScheme.primary
        ProjectStatus.PENDING -> Warning
        ProjectStatus.DONE -> MaterialTheme.colorScheme.outline
    }


object ProjectPreviewData {

    val projects = listOf(
        ProjectUiModel(
            id = 1,
            category = ProjectCategory.DEVELOPMENT,
            status = ProjectStatus.ACTIVE,
            title = "Neural Engine Alpha",
            description = "High-performance inference engine...",
            progress = 78,
            footerText = "Optimization Progress",
            membersCount = 2
        ),

        ProjectUiModel(
            id = 2,
            category = ProjectCategory.DESIGN,
            status = ProjectStatus.PENDING,
            title = "Lumina Design System",
            description = "Unified token-based architecture...",
            showImagePlaceholder = true,
            footerText = "Review: Oct 24"
        )
    )
}