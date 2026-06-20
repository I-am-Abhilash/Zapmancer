package com.smach.zapmancer.features.projects.screen

//import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
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
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import androidx.compose.runtime.Composable
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.koin.compose.viewmodel.koinViewModel
import com.smach.zapmancer.features.projects.viewmodel.ProjectListViewModel
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.theme.ZapGrey
import com.smach.zapmancer.features.common.theme.ZapOrange
import com.smach.zapmancer.features.projects.state.ProjectListUiState
import com.smach.zapmancer.features.projects.viewmodel.ProjectListEvent

@Composable
fun ProjectListScreen(
    viewModel: ProjectListViewModel = koinViewModel(),
    onProjectClick: (Int) -> Unit = {},
) {
    val state by viewModel.uiState.collectAsState()

    ProjectListScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onProjectClick = onProjectClick,
    )
}

@Composable
fun ProjectListScreen(
    state: ProjectListUiState,
    onEvent: (ProjectListEvent) -> Unit,
    onProjectClick: (Int) -> Unit = {},
) {
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
    onEvent: (ProjectListEvent) -> Unit,
    onProjectClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit
) {
    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
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
            item {
                Spacer(modifier = Modifier.height(8.dp))
                PortfolioHeader()
            }

            item {
                CategoryTabs(
                    categories = listOf("All", "Development", "Design"),
                    selectedCategory = state.selectedCategory,
                    onCategorySelected = {
                        onEvent(
                            ProjectListEvent.CategorySelected(it)
                        )
                    }                )
            }

            val projects = state.projects.filter {
                state.selectedCategory == "All" ||
                        it.category == state.selectedCategory
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
            color = Color(0xFF1A1C1E)
        )
        Spacer(modifier = Modifier.height(8.dp))
        DashedDivider()
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Manage and track your active development and design cycles.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF5F6368)
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
fun CategoryTabs(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onCategorySelected(category) },
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    color = if (isSelected) Color.White else Color(0xFF5F6368),
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ProjectItemCard(project: ProjectUiModel, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier.drawAccentLine(project.accentColor).padding(16.dp)
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
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            project.icon,
                            contentDescription = null,
                            tint = project.accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            project.category.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            color = project.accentColor,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            project.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1C1E)
                        )
                    }
                }

                Surface(
                    color = if (project.status == "ACTIVE") MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else project.accentColor.copy(
                        alpha = 0.1f
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    val statusColor = when (project.status) {
                        "ACTIVE" -> MaterialTheme.colorScheme.primary
                        "PENDING" -> ZapOrange
                        "DONE" -> ZapGrey
                        else -> project.accentColor
                    }
                    Text(
                        project.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF44474E),
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
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF44474E)
                    )
                    Text(
                        "${project.progress}%",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF44474E)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                LinearProgressIndicator(
                    progress = { project.progress / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
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
                            shape = RoundedCornerShape(8.dp),
                            border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
                        ) {
                            Text(
                                tag,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF5F6368),
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
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1A1C1E))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        ZapOrange.copy(alpha = 0.3f),
                                        Color.Transparent
                                    ),
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
                                .border(2.dp, Color.White, CircleShape),
                            shape = CircleShape,
                            color = Color.LightGray
                        ) {}
                        Spacer(modifier = Modifier.width((-8).dp))
                    }
                    if (project.membersCount > 2) {
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .border(2.dp, Color.White, CircleShape),
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
                        color = Color(0xFF5F6368),
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
    val category: String,
    val status: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color,
    val progress: Int? = null,
    val tags: List<String> = emptyList(),
    val showImagePlaceholder: Boolean = false,
    val footerText: String? = null,
    val footerIcon: ImageVector? = null,
    val membersCount: Int = 0
)


object ProjectPreviewData {

    val projects = listOf(
        ProjectUiModel(
            id = 1,
            category = "Development",
            status = "ACTIVE",
            title = "Neural Engine Alpha",
            description = "High-performance inference engine...",
            icon = Icons.Outlined.Devices,
            accentColor = Color(0xFF2BA8A2),
            progress = 78,
            footerText = "Optimization Progress",
            membersCount = 2
        ),

        ProjectUiModel(
            id = 2,
            category = "Design",
            status = "PENDING",
            title = "Lumina Design System",
            description = "Unified token-based architecture...",
            icon = Icons.Outlined.Palette,
            accentColor = ZapOrange,
            showImagePlaceholder = true,
            footerText = "Review: Oct 24"
        )
    )
}