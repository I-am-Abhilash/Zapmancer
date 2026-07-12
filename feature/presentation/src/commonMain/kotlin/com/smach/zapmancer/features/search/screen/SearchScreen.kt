package com.smach.zapmancer.features.search.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.smach.zapmancer.domain.model.Project
import com.smach.zapmancer.domain.model.ProjectCategory
import com.smach.zapmancer.domain.model.ProjectStatus
import com.smach.zapmancer.features.common.components.EmptyState
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.search.state.SearchSortOption
import com.smach.zapmancer.features.search.state.SearchUiState
import com.smach.zapmancer.features.search.viewmodel.SearchEffect
import com.smach.zapmancer.features.search.viewmodel.SearchEvent
import com.smach.zapmancer.features.search.viewmodel.SearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: SearchViewModel,
    onBackClick: () -> Unit,
    onProjectClick: (String) -> Unit,
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is SearchEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }

                is SearchEffect.NavigateToProjectDetail -> {
                    onProjectClick(effect.projectId)
                }

                SearchEffect.NavigateBack -> {
                    onBackClick()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        SearchContent(
            modifier = Modifier.padding(paddingValues),
            state = state,
            onEvent = viewModel::onEvent,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    modifier: Modifier = Modifier,
    state: SearchUiState,
    onEvent: (SearchEvent) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1
            lastVisibleItemIndex >= totalItemsNumber - 2 && totalItemsNumber > 0 && state.hasMore && !state.isLoading
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onEvent(SearchEvent.LoadNextPage)
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
//            IconButton(
//                onClick = { onEvent(SearchEvent.BackClicked) },
//                modifier = Modifier.semantics { contentDescription = "Go Back" },
//            ) {
//                Icon(
//                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.onBackground,
//                )
//            }

            Spacer(modifier = Modifier.width(4.dp))

            TextField(
                value = state.query,
                onValueChange = { onEvent(SearchEvent.QueryChanged(it)) },
                placeholder = { Text("Search projects...", color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)) },
                modifier = Modifier
                    .weight(1f)
                    .focusRequester(focusRequester)
                    .semantics { contentDescription = "Search input field" },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
                shape = RoundedCornerShape(28.dp),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (state.query.isNotEmpty()) {
                            IconButton(
                                onClick = { onEvent(SearchEvent.ClearQuery) },
                                modifier = Modifier.semantics { contentDescription = "Clear text" },
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                        }
                        IconButton(
                            onClick = { onEvent(SearchEvent.StartVoiceSearch) },
                            modifier = Modifier.semantics { contentDescription = "Voice search" },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                },
            )
        }

        SearchFilterHeader(
            selectedCategory = state.categoryFilter,
            selectedSort = state.sortBy,
            onCategoryChange = { onEvent(SearchEvent.ChangeCategoryFilter(it)) },
            onSortChange = { onEvent(SearchEvent.ChangeSortOption(it)) },
        )

        if (state.isRefreshing) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.4f)
                        .height(2.dp)
                        .background(MaterialTheme.colorScheme.primary),
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            if (state.query.isEmpty()) {
                SearchInitialView(
                    recent = state.recentSearches,
                    trending = state.trendingSearches,
                    onQuerySelected = { query ->
                        onEvent(SearchEvent.QueryChanged(query))
                        onEvent(SearchEvent.AddRecentSearch(query))
                    },
                    onRemoveHistory = { onEvent(SearchEvent.RemoveRecentSearch(it)) },
                    onClearHistory = { onEvent(SearchEvent.ClearRecentSearches) },
                )
            } else {
                if (state.searchResults.isEmpty() && !state.isLoading && state.error == null) {
                    EmptyState(
                        title = "No results match \"${state.query}\"",
                        description = "Try refining your search keyword or selecting a different category filter.",
                        icon = Icons.Outlined.Devices,
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(state.searchResults, key = { it.id }) { project ->
                            SearchResultCard(
                                project = project,
                                query = state.query,
                                onClick = {
                                    onEvent(SearchEvent.ProjectClicked(project.id))
                                },
                            )
                        }

                        if (state.isLoading) {
                            items(3) {
                                SearchSkeletonCard()
                            }
                        }
                    }
                }
            }
        }
    }

    if (state.isListening) {
        VoiceSearchListeningDialog(
            onDismiss = { onEvent(SearchEvent.StopVoiceSearch) },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchFilterHeader(
    selectedCategory: ProjectCategory,
    selectedSort: SearchSortOption,
    onCategoryChange: (ProjectCategory) -> Unit,
    onSortChange: (SearchSortOption) -> Unit,
) {
    var sortMenuExpanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LazyRow(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 8.dp),
        ) {
            items(ProjectCategory.entries.toList()) { category ->
                val isSelected = selectedCategory == category
                val tintColor = when (category) {
                    ProjectCategory.ALL -> MaterialTheme.colorScheme.primary
                    ProjectCategory.DESIGN -> MaterialTheme.colorScheme.tertiary
                    ProjectCategory.DEVELOPMENT -> MaterialTheme.colorScheme.primary
                    ProjectCategory.MARKETING -> MaterialTheme.colorScheme.secondary
                    ProjectCategory.WRITING -> MaterialTheme.colorScheme.secondary
                    ProjectCategory.MULTIMEDIA -> MaterialTheme.colorScheme.tertiary
                    ProjectCategory.CONSULTING -> MaterialTheme.colorScheme.primary
                    ProjectCategory.ADMIN -> MaterialTheme.colorScheme.outline
                    ProjectCategory.FINANCE -> MaterialTheme.colorScheme.primary
                    ProjectCategory.LEGAL -> MaterialTheme.colorScheme.tertiary
                    ProjectCategory.ANALYTICS -> MaterialTheme.colorScheme.secondary
                    ProjectCategory.SECURITY -> MaterialTheme.colorScheme.primary
                    ProjectCategory.CUSTOMER_SUPPORT -> MaterialTheme.colorScheme.outline
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
                        color = if (isSelected) tintColor.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(
                            alpha = 0.5f
                        )
                    ),
                    shape = RoundedCornerShape(20.dp),
                )
            }
        }

        Box {
            IconButton(onClick = { sortMenuExpanded = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = "Sort Options",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            DropdownMenu(
                expanded = sortMenuExpanded,
                onDismissRequest = { sortMenuExpanded = false },
            ) {
                SearchSortOption.entries.forEach { option ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = option.displayName,
                                    fontWeight = if (selectedSort == option) FontWeight.Bold else FontWeight.Normal,
                                    color = if (selectedSort == option) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                )
                            }
                        },
                        onClick = {
                            onSortChange(option)
                            sortMenuExpanded = false
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchInitialView(
    recent: List<String>,
    trending: List<String>,
    onQuerySelected: (String) -> Unit,
    onRemoveHistory: (String) -> Unit,
    onClearHistory: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
    ) {
        if (recent.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Recent Searches",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Clear All",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable { onClearHistory() }
                            .semantics { contentDescription = "Clear all recent search history" },
                    )
                }
            }

            items(recent) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onQuerySelected(item) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = item,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(
                        onClick = { onRemoveHistory(item) },
                        modifier = Modifier
                            .size(24.dp)
                            .semantics { contentDescription = "Remove \"$item\" from history" },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Trending Searches",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 24.dp, bottom = 12.dp),
            )

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                trending.forEach { term ->
                    Surface(
                        modifier = Modifier
                            .clickable { onQuerySelected(term) }
                            .semantics { contentDescription = "Trending keyword: $term" },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = term,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun shimmerBrush(
    showShimmer: Boolean = true,
    targetValue: Float = 1000f
): Brush {
    return if (showShimmer) {
        val transition = rememberInfiniteTransition()
        val translateAnimation by transition.animateFloat(
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            ),
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset(x = translateAnimation, y = translateAnimation)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = androidx.compose.ui.geometry.Offset.Zero,
            end = androidx.compose.ui.geometry.Offset.Zero
        )
    }
}

@Composable
private fun SearchResultCard(
    project: Project,
    query: String,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .semantics { contentDescription = "Project details: ${project.title}" },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = highlightText(project.title, query, MaterialTheme.colorScheme.primary),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Category Tag
                Box(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = project.category.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = highlightText(project.description, query, MaterialTheme.colorScheme.primary),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            if (project.tags.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    project.tags.take(3).forEach { tag ->
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .border(
                                    width = 0.5.dp,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = highlightText("#$tag", query, MaterialTheme.colorScheme.primary),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Progress bar if progress is available
            project.progress?.let { progressValue ->
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$progressValue%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                androidx.compose.material3.LinearProgressIndicator(
                    progress = { progressValue / 100f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            
            // Bottom Metadata row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Status indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val statusColor = when (project.status) {
                        ProjectStatus.ACTIVE -> Color(0xFF4CAF50)
                        ProjectStatus.PENDING -> Color(0xFFFF9800)
                        ProjectStatus.DONE -> Color(0xFF2196F3)
                    }
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Text(
                        text = project.status.displayName,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Other metadata (members, footer text)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (project.membersCount > 0) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = project.membersCount.toString(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    project.footerText?.let { footer ->
                        Text(
                            text = footer,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchSkeletonCard() {
    val brush = shimmerBrush()
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f),
        ),
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(18.dp)
                    .background(brush, RoundedCornerShape(4.dp)),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(14.dp)
                    .background(brush, RoundedCornerShape(4.dp)),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(14.dp)
                    .background(brush, RoundedCornerShape(4.dp)),
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 20.dp)
                        .background(brush, RoundedCornerShape(10.dp)),
                )
                Box(
                    modifier = Modifier
                        .size(width = 60.dp, height = 20.dp)
                        .background(brush, RoundedCornerShape(10.dp)),
                )
            }
        }
    }
}

@Composable
private fun VoiceSearchListeningDialog(
    onDismiss: () -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier
                    .padding(32.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "Listening...",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Speak clearly into your microphone",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(160.dp),
                ) {
                    // Pulsing Outer Wave 3
                    val wave3Scale by infiniteTransition.animateFloat(
                        initialValue = 1.0f,
                        targetValue = 2.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, delayMillis = 400),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    val wave3Alpha by infiniteTransition.animateFloat(
                        initialValue = 0.4f,
                        targetValue = 0.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1500, delayMillis = 400),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(72.dp * wave3Scale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = wave3Alpha)),
                    )

                    // Pulsing Wave 2
                    val wave2Scale by infiniteTransition.animateFloat(
                        initialValue = 1.0f,
                        targetValue = 1.6f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, delayMillis = 100),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    val wave2Alpha by infiniteTransition.animateFloat(
                        initialValue = 0.6f,
                        targetValue = 0.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(1200, delayMillis = 100),
                            repeatMode = RepeatMode.Restart
                        )
                    )
                    Box(
                        modifier = Modifier
                            .size(72.dp * wave2Scale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = wave2Alpha)),
                    )

                    // Base Pulsing Wave 1
                    Box(
                        modifier = Modifier
                            .size(72.dp * pulseScale)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    )

                    Surface(
                        modifier = Modifier.size(72.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        tonalElevation = 4.dp,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(36.dp),
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(24.dp),
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

private fun highlightText(text: String, query: String, highlightColor: Color): AnnotatedString {
    if (query.isEmpty()) return AnnotatedString(text)
    return buildAnnotatedString {
        var startIdx = 0
        while (startIdx < text.length) {
            val matchIdx = text.indexOf(query, startIdx, ignoreCase = true)
            if (matchIdx == -1) {
                append(text.substring(startIdx))
                break
            }
            if (matchIdx > startIdx) {
                append(text.substring(startIdx, matchIdx))
            }
            withStyle(style = SpanStyle(color = highlightColor, fontWeight = FontWeight.Bold)) {
                append(text.substring(matchIdx, matchIdx + query.length))
            }
            startIdx = matchIdx + query.length
        }
    }
}

@Preview
@Composable
fun SearchScreenPreview() {
    AppTheme {
        SearchContent(
            state = SearchUiState(
                query = "Ktor",
                searchResults = listOf(
                    Project(
                        id = "1",
                        category = ProjectCategory.DEVELOPMENT,
                        status = ProjectStatus.ACTIVE,
                        title = "Ktor API Integration",
                        description = "Looking for a backend developer to implement type-safe API endpoints using Ktor Resources.",
                        tags = listOf("Ktor", "Kotlin", "Backend"),
                        progress = 65,
                        membersCount = 3,
                        footerText = "Budget: $1,200"
                    ),
                    Project(
                        id = "2",
                        category = ProjectCategory.DEVELOPMENT,
                        status = ProjectStatus.ACTIVE,
                        title = "Compose Multiplatform Search Screen",
                        description = "Implement an optimized search screen with preview support, search filtering and pagination.",
                        tags = listOf("Compose", "KMP", "UI"),
                        progress = 12,
                        membersCount = 1,
                        footerText = "Budget: $450"
                    ),
                ),
            ),
            onEvent = {},
        )
    }
}
