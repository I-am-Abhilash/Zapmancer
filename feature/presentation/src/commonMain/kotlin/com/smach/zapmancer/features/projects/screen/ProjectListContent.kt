//package com.smach.zapmancer.features.projects.screen
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Canvas
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.outlined.Campaign
//import androidx.compose.material.icons.outlined.Devices
//import androidx.compose.material.icons.outlined.Palette
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.LinearProgressIndicator
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.OutlinedTextFieldDefaults
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalFocusManager
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.drawBehind
//import androidx.compose.ui.geometry.Offset
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.PathEffect
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.smach.zapmancer.domain.model.ProjectCategory
//import com.smach.zapmancer.domain.model.ProjectStatus
//import com.smach.zapmancer.features.alerts.screen.drawAccentLine
//import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
//import com.smach.zapmancer.features.projects.state.ProjectListUiState
//import com.smach.zapmancer.features.projects.viewmodel.ProjectListEvent
//
//@Composable
//fun ProjectListContent(
//    state: ProjectListUiState,
//    onEvent: (ProjectListEvent) -> Unit,
//    onProjectClick: (Int) -> Unit,
//    onSearchClick: () -> Unit = {},
//    onProfileClick: (String) -> Unit = {},
//    onCreateProjectClick: () -> Unit = {},
//    showTopBar: Boolean = true,
//) {
//    val focusManager = LocalFocusManager.current
//    LaunchedEffect(Unit) { focusManager.clearFocus() }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        if (showTopBar) {
//            TopAppBar(
//                onCreateProjectClick = onCreateProjectClick,
//                onSearchClick = onSearchClick,
//                onCategoryClick = { category -> onEvent(ProjectListEvent.CategorySelected(category)) },
//                selectedCategory = state.category,
//            )
//        }
//        ProjectListPane(
//            state = state,
//            onEvent = onEvent,
//            onProjectClick = onProjectClick,
//        )
//    }
//}
//
//@Composable
//fun TopAppBar(
//    onCreateProjectClick: () -> Unit,
//    onSearchClick: () -> Unit,
//    onCategoryClick: (ProjectCategory) -> Unit,
//    selectedCategory: ProjectCategory,
//) {
//    Surface(
//        color = MaterialTheme.colorScheme.surface,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp),
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically,
//        ) {
//            Text(
//                text = "Zapmancer",
//                fontWeight = FontWeight.Bold,
//                fontSize = 20.sp,
//                color = MaterialTheme.colorScheme.primary,
//            )
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            // Search and filter
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp),
//            ) {
//                OutlinedTextField(
//                    value = "", // placeholder - in real implementation would come from state
//                    onValueChange = { /* handled by search button */ },
//                    label = { Text("Search") },
//                    leadingIcon = {
//                        IconButton(onClick = onSearchClick) {
//                            Icon(
//                                imageVector = Icons.Default.Search,
//                                contentDescription = "Search",
//                                modifier = Modifier.size(24.dp),
//                            )
//                        }
//                    },
//                    colors = OutlinedTextFieldDefaults.colors(
//                        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
//                        focusedBorderColor = MaterialTheme.colorScheme.primary,
//                        unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
//                    ),
//                    modifier = Modifier
//                        .width(180.dp)
//                        .height(36.dp),
//                )
//
//                // Category chips
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp),
//                ) {
//                    ProjectCategory.values().forEach { category ->
//                        val isSelected = category == selectedCategory
//                        Surface(
//                            onClick = { onCategoryClick(category) },
//                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
//                            contentColor = if (isSelected) androidx.compose.ui.graphics.Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
//                            shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
//                            modifier = Modifier.size(height = 32.dp, width = 72.dp),
//                        ) {
//                            Text(
//                                text = category.displayName,
//                                fontSize = 12.sp,
//                                fontWeight = FontWeight.Bold,
//                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
//                            )
//                        }
//                    }
//                }
//            }
//
//            Spacer(modifier = Modifier.width(8.dp))
//
//            // New project button
//            Button(
//                onClick = onCreateProjectClick,
//                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
//                shape = MaterialTheme.shapes.small,
//                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "New Project",
//                    tint = Color.White,
//                    modifier = Modifier.size(20.dp),
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ProjectListPane(
//    state: ProjectListUiState,
//    onEvent: (ProjectListEvent) -> Unit,
//    onProjectClick: (Int) -> Unit,
//) {
//    val windowLayout = rememberWindowLayout()
//    val projects = if (state.category == ProjectCategory.ALL) {
//        state.projects
//    } else {
//        state.projects.filter { it.category == state.category }
//    }
//
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = windowLayout.screenHorizontalPaddingDp.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//        contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp),
//    ) {
//        if (state.isLoading) {
//            item {
//                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))
//            }
//        }
//        if (state.error != null) {
//            item {
//                Text(
//                    text = state.error,
//                    color = MaterialTheme.colorScheme.error,
//                    style = MaterialTheme.typography.bodyMedium,
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
//                    textAlign = TextAlign.Center,
//                )
//            }
//        }
//
//        item {
//            Spacer(modifier = Modifier.height(8.dp))
//            PortfolioHeader()
//        }
//
//        if (projects.isEmpty() && !state.isLoading && state.error == null) {
//            item {
//                Text(
//                    text = "No projects found for this category.",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
//                    textAlign = TextAlign.Center,
//                )
//            }
//        }
//
//        items(projects) { project ->
//            ProjectItemCard(project = project, onClick = { onProjectClick(project.id) })
//        }
//
//        item { Spacer(modifier = Modifier.height(24.dp)) }
//    }
//}
//
//// @Composable
//// private fun PortfolioHeader() {
////    Column {
////        Text(
////            "Get Projects ",
////            style = MaterialTheme.typography.headlineMedium,
////            fontWeight = FontWeight.Bold,
////            color = MaterialTheme.colorScheme.onSurface,
////        )
////        Spacer(modifier = Modifier.height(8.dp))
////        DashedDivider()
////        Spacer(modifier = Modifier.height(12.dp))
////        Text(
////            "Manage and track your active development and design cycles.",
////            style = MaterialTheme.typography.bodyMedium,
////            color = MaterialTheme.colorScheme.onSurfaceVariant,
////        )
////        Spacer(modifier = Modifier.height(8.dp))
////    }
//// }
//
//// @Composable
//// private fun DashedDivider() {
////    val drawLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
////    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
////        drawLine(
////            color = drawLineColor,
////            start = Offset(0f, 0f),
////            end = Offset(size.width, 0f),
////            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f),
////        )
////    }
//// }
//
//@Composable
//fun ProjectItemCard(project: ProjectUiModel, onClick: () -> Unit = {}) {
//    Card(
//        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
//        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
//        shape = MaterialTheme.shapes.large,
//        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
//        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
//    ) {
//        val icon = when (project.category) {
//            ProjectCategory.DESIGN -> Icons.Outlined.Palette
//            ProjectCategory.DEVELOPMENT -> Icons.Outlined.Devices
//            ProjectCategory.MARKETING -> Icons.Outlined.Campaign
//            ProjectCategory.ALL -> Icons.Outlined.Devices
//        }
//
//        val accentColor = when (project.category) {
//            ProjectCategory.DESIGN -> MaterialTheme.colorScheme.tertiary
//            ProjectCategory.DEVELOPMENT -> MaterialTheme.colorScheme.primary
//            ProjectCategory.MARKETING -> MaterialTheme.colorScheme.secondary
//            ProjectCategory.ALL -> MaterialTheme.colorScheme.primary
//        }
//
//        Column(modifier = Modifier.drawAccentLine(accentColor).padding(16.dp)) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.Top,
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Box(
//                        modifier = Modifier
//                            .size(40.dp)
//                            .clip(MaterialTheme.shapes.small)
//                            .background(MaterialTheme.colorScheme.background),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
//                    }
//                    Spacer(modifier = Modifier.width(12.dp))
//                    Column {
//                        Text(
//                            text = project.category.displayName,
//                            style = MaterialTheme.typography.labelSmall,
//                            color = accentColor,
//                            fontWeight = FontWeight.Bold,
//                            letterSpacing = 0.5.sp,
//                        )
//                        Text(
//                            project.title,
//                            style = MaterialTheme.typography.titleMedium,
//                            fontWeight = FontWeight.Bold,
//                            color = MaterialTheme.colorScheme.onSurface,
//                        )
//                    }
//                }
//
//                Surface(
//                    color = if (project.status == ProjectStatus.ACTIVE) {
//                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
//                    } else {
//                        accentColor.copy(alpha = 0.1f)
//                    },
//                    shape = MaterialTheme.shapes.small,
//                ) {
//                    Text(
//                        project.status.displayName,
//                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
//                        style = MaterialTheme.typography.labelSmall,
//                        color = project.status.color(),
//                        fontWeight = FontWeight.ExtraBold,
//                    )
//                }
//
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    project.description,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    lineHeight = 20.sp,
//                )
//
//                if (project.progress != null) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween,
//                    ) {
//                        Text(
//                            "Optimization Progress",
//                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                        Text(
//                            "${project.progress}%",
//                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                        )
//                    }
//                    Spacer(modifier = Modifier.height(6.dp))
//                    LinearProgressIndicator(
//                        progress = { project.progress / 100f },
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(8.dp)
//                            .clip(MaterialTheme.shapes.extraSmall),
//                        color = MaterialTheme.colorScheme.primary,
//                        trackColor = MaterialTheme.colorScheme.background,
//                    )
//                }
//
//                if (project.tags.isNotEmpty()) {
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//                        project.tags.forEach { tag ->
//                            Surface(
//                                color = MaterialTheme.colorScheme.background,
//                                shape = MaterialTheme.shapes.small,
//                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
//                            ) {
//                                Text(
//                                    tag,
//                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
//                                    style = MaterialTheme.typography.labelSmall,
//                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                                    fontWeight = FontWeight.Medium,
//                                )
//                            }
//                        }
//                    }
//                }
//
//                if (project.showImagePlaceholder) {
//                    Spacer(modifier = Modifier.height(12.dp))
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .height(160.dp)
//                            .clip(MaterialTheme.shapes.medium)
//                            .background(MaterialTheme.colorScheme.surfaceVariant),
//                    ) {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxSize()
//                                .background(Brush.radialGradient(center = Offset(400f, 200f), radius = 300f)),
//                        )
//                    }
//                }
//
//                if (project.membersCount > 0) {
//                    Spacer(modifier = Modifier.height(16.dp))
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        repeat(2) {
//                            Surface(
//                                modifier = Modifier
//                                    .size(28.dp)
//                                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
//                                shape = CircleShape,
//                                color = MaterialTheme.colorScheme.surfaceVariant,
//                            ) {}
//                            Spacer(modifier = Modifier.width((-8).dp))
//                        }
//                        if (project.membersCount > 2) {
//                            Surface(
//                                modifier = Modifier
//                                    .size(28.dp)
//                                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
//                                shape = CircleShape,
//                                color = MaterialTheme.colorScheme.background,
//                            ) {
//                                Box(contentAlignment = Alignment.Center) {
//                                    Text(
//                                        "+${project.membersCount - 2}",
//                                        style = MaterialTheme.typography.labelSmall,
//                                        fontWeight = FontWeight.Bold,
//                                        fontSize = 10.sp,
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(16.dp))
//                HorizontalDivider(color = MaterialTheme.colorScheme.background, thickness = 1.dp)
//                Spacer(modifier = Modifier.height(12.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                ) {
//                    Row(verticalAlignment = Alignment.CenterVertically) {
//                        if (project.footerIcon != null) {
//                            Icon(
//                                project.footerIcon,
//                                contentDescription = null,
//                                tint = MaterialTheme.colorScheme.primary,
//                                modifier = Modifier.size(16.dp),
//                            )
//                            Spacer(modifier = Modifier.width(6.dp))
//                        }
//                        Text(
//                            project.footerText ?: "",
//                            style = MaterialTheme.typography.labelMedium,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            fontWeight = FontWeight.Medium,
//                        )
//                    }
//
//                    Icon(
//                        Icons.AutoMirrored.Filled.KeyboardArrowRight,
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.primary,
//                        modifier = Modifier.size(24.dp),
//                    )
//                }
//            }
//        }
//    }
//}
