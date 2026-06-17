//package com.smach.zapmancer.features.home.screen
//
//import androidx.compose.foundation.background
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
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FilterChip
//import androidx.compose.material3.FilterChipDefaults
//import androidx.compose.material3.HorizontalDivider
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.pulltorefresh.PullToRefreshBox
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import com.smach.zapmancer.domain.model.Article
//import com.smach.zapmancer.features.common.components.AppImage
//import com.smach.zapmancer.features.common.components.EmptyState
//import com.smach.zapmancer.features.common.components.FeaturedArticleShimmer
//import com.smach.zapmancer.features.common.components.RecommendedArticleShimmer
//import com.smach.zapmancer.features.home.viewmodel.HomeEvent
//import com.smach.zapmancer.features.home.viewmodel.HomeViewModel
//import org.koin.compose.viewmodel.koinViewModel
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeScreen(
//    onArticleClick: (Int) -> Unit,
//    onSearchClick: () -> Unit,
//    viewModel: HomeViewModel = koinViewModel(),
//) {
//    val state by viewModel.uiState.collectAsStateWithLifecycle()
//
//    HomeContent(
//        state = state,
//        onArticleClick = onArticleClick,
//        onRefresh = { viewModel.onEvent(HomeEvent.Refresh) },
//        onLoadMore = { viewModel.onEvent(HomeEvent.LoadNextArticles) },
//        onCategorySelect = { viewModel.onEvent(HomeEvent.SelectCategory(it)) },
//        onSearchIconClick = onSearchClick,
//        onMenuClick = { /* TODO */ },
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HomeContent(
//    state: HomeUiState,
//    onArticleClick: (Int) -> Unit,
//    onRefresh: () -> Unit,
//    onLoadMore: () -> Unit,
//    onCategorySelect: (String) -> Unit,
//    onSearchIconClick: () -> Unit,
//    onMenuClick: () -> Unit,
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                actions = {
//                    IconButton(onClick = onSearchIconClick) {
//                        Icon(Icons.Filled.Search, contentDescription = "Search")
//                    }
//                },
//                navigationIcon = {
//                    IconButton(onClick = onMenuClick) {
//                        Icon(Icons.Filled.Menu, contentDescription = "Menu")
//                    }
//                },
//                title = {
//                    Text(
//                        "ScriptSide",
//                        style = MaterialTheme.typography.headlineSmall,
//                        fontWeight = FontWeight.ExtraBold,
//                        color = MaterialTheme.colorScheme.onBackground,
//                    )
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = MaterialTheme.colorScheme.background,
//                ),
//            )
//        },
//    ) { padding ->
//        PullToRefreshBox(
//            isRefreshing = state.isRefreshing,
//            onRefresh = onRefresh,
//            modifier = Modifier.padding(padding),
//        ) {
//            if (state.isLoading && state.recommendedArticles.isEmpty()) {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.background),
//                ) {
//                    item { FeaturedArticleShimmer() }
//                    items(5) { RecommendedArticleShimmer() }
//                }
//            } else if (!state.isLoading && !state.isRefreshing && state.recommendedArticles.isEmpty()) {
//                EmptyState(
//                    title = "No articles found",
//                    description = "Try refreshing or check back later for new stories.",
//                    buttonText = "Refresh",
//                    onButtonClick = onRefresh,
//                )
//            } else {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .background(MaterialTheme.colorScheme.background),
//                ) {
//                    if (state.recommendedArticles.isNotEmpty()) {
//                        item {
//                            val featuredArticle = state.recommendedArticles.first()
//                            FeaturedArticleItem(featuredArticle) {
//                                onArticleClick(featuredArticle.id)
//                            }
//                        }
//                    }
//
//                    item {
//                        CategoryRow(
//                            categories = state.categories,
//                            selectedCategory = state.selectedCategory,
//                            onCategorySelect = onCategorySelect,
//                        )
//                        HorizontalDivider(
//                            modifier = Modifier.fillMaxWidth(),
//                            thickness = 0.5.dp,
//                            color = MaterialTheme.colorScheme.outlineVariant,
//                        )
//                    }
//
//                    val mainFeedStart = if (state.recommendedArticles.isNotEmpty()) 1 else 0
//                    val firstBatchSize = 9
//                    val firstBatchEnd =
//                        (mainFeedStart + firstBatchSize).coerceAtMost(state.recommendedArticles.size)
//
//                    items(
//                        state.recommendedArticles.subList(
//                            mainFeedStart,
//                            firstBatchEnd,
//                        ),
//                    ) { article ->
//                        RecommendedArticleItem(article) {
//                            onArticleClick(article.id)
//                        }
//                    }
//
//                    if (firstBatchEnd < state.recommendedArticles.size || !state.endReached) {
//                        item {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(vertical = 16.dp),
//                                contentAlignment = Alignment.Center,
//                            ) {
//                                OutlinedButton(
//                                    onClick = onLoadMore,
//                                    shape = RoundedCornerShape(50),
//                                    border = ButtonDefaults.outlinedButtonBorder(enabled = true)
//                                        .copy(
//                                            width = 1.dp,
//                                            brush = SolidColor(
//                                                MaterialTheme.colorScheme.outline.copy(
//                                                    alpha = 0.5f,
//                                                ),
//                                            ),
//                                        ),
//                                ) {
//                                    Text(
//                                        "See more",
//                                        style = MaterialTheme.typography.labelLarge,
//                                        color = MaterialTheme.colorScheme.onSurface,
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                    if (state.trendingArticles.isNotEmpty()) {
//                        item {
//                            SectionTitle("Trending on ScriptSide")
//                            TrendingNumberedList(state.trendingArticles.take(6)) { articleId ->
//                                onArticleClick(articleId)
//                            }
//                            Spacer(modifier = Modifier.height(24.dp))
//                            HorizontalDivider(
//                                modifier = Modifier.padding(horizontal = 24.dp),
//                                thickness = 0.5.dp,
//                                color = MaterialTheme.colorScheme.outlineVariant,
//                            )
//                        }
//                    }
//
//                    // 6. Remaining Feed
//                    if (firstBatchEnd < state.recommendedArticles.size) {
//                        val remainingArticles = state.recommendedArticles.subList(
//                            firstBatchEnd,
//                            state.recommendedArticles.size,
//                        )
//                        itemsIndexed(remainingArticles) { index, article ->
//                            if (index >= remainingArticles.size - 1 && !state.endReached && !state.isPaging) {
//                                onLoadMore()
//                            }
//                            RecommendedArticleItem(article) {
//                                onArticleClick(article.id)
//                            }
//                        }
//                    }
//
//                    if (state.isPaging) {
//                        item {
//                            Box(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(24.dp),
//                                contentAlignment = Alignment.Center,
//                            ) {
//                                CircularProgressIndicator(
//                                    modifier = Modifier.size(24.dp),
//                                    strokeWidth = 2.dp,
//                                )
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FeaturedArticleItem(
//    article: Article,
//    onArticleClick: (Int) -> Unit,
//) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable { onArticleClick(article.id) }
//            .padding(bottom = 24.dp),
//    ) {
//        AppImage(
//            model = article.thumbnailUrl,
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(240.dp),
//            contentScale = ContentScale.Crop,
//        )
//
//        Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)) {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                AppImage(
//                    model = article.authorAvatarUrl,
//                    contentDescription = null,
//                    modifier = Modifier.size(24.dp).clip(CircleShape),
//                    shape = CircleShape,
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = article.authorName,
//                    style = MaterialTheme.typography.labelMedium,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = article.title,
//                style = MaterialTheme.typography.headlineMedium,
//                fontWeight = FontWeight.ExtraBold,
//                maxLines = 3,
//                overflow = TextOverflow.Ellipsis,
//                lineHeight = 32.sp,
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = article.content,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//            )
//
//            Spacer(modifier = Modifier.height(12.dp))
//
//            Text(
//                text = "${article.publishedDate} • ${article.readingTimeMinutes} min read",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//        }
//    }
//}
//
//@Composable
//fun CategoryRow(
//    categories: List<String>,
//    selectedCategory: String,
//    onCategorySelect: (String) -> Unit,
//) {
//    LazyRow(
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        items(categories) { category ->
//            FilterChip(
//                selected = category == selectedCategory,
//                onClick = { onCategorySelect(category) },
//                label = { Text(category) },
//                colors = FilterChipDefaults.filterChipColors(
//                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
//                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
//                ),
//                border = null,
//            )
//        }
//    }
//}
//
//@Composable
//fun TrendingNumberedList(
//    articles: List<Article>,
//    onArticleClick: (Int) -> Unit,
//) {
//    Column(
//        modifier = Modifier.padding(horizontal = 24.dp),
//        verticalArrangement = Arrangement.spacedBy(16.dp),
//    ) {
//        articles.forEachIndexed { index, article ->
//            val actualIndex = index + 1
//            TrendingNumberedItem(
//                index = if (actualIndex < 10) "0$actualIndex" else "$actualIndex",
//                article = article,
//                onArticleClick = { onArticleClick(article.id) },
//            )
//        }
//    }
//}
//
//@Composable
//fun TrendingNumberedItem(
//    modifier: Modifier = Modifier,
//    index: String,
//    article: Article,
//    onArticleClick: (Int) -> Unit,
//) {
//    Row(
//        modifier = modifier
//            .clickable { onArticleClick(article.id) }
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.Top,
//    ) {
//        Text(
//            text = index,
//            style = MaterialTheme.typography.headlineMedium,
//            fontWeight = FontWeight.Black,
//            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
//        )
//        Spacer(modifier = Modifier.width(16.dp))
//        Column {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                AppImage(
//                    model = article.authorAvatarUrl,
//                    contentDescription = null,
//                    modifier = Modifier.size(16.dp).clip(CircleShape),
//                    shape = CircleShape,
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = article.authorName,
//                    style = MaterialTheme.typography.labelSmall,
//                    fontWeight = FontWeight.Bold,
//                )
//            }
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = article.title,
//                style = MaterialTheme.typography.titleMedium,
//                fontWeight = FontWeight.Bold,
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                lineHeight = 20.sp,
//            )
//            Spacer(modifier = Modifier.height(4.dp))
//            Text(
//                text = "${article.publishedDate} • ${article.readingTimeMinutes} min read",
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onSurfaceVariant,
//            )
//        }
//    }
//}
//
//@Composable
//fun SectionTitle(title: String) {
//    Row(
//        modifier = Modifier.padding(horizontal = 24.dp, vertical = 24.dp),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        Icon(
//            imageVector = Icons.Default.Menu, // Placeholder for a trending icon
//            contentDescription = null,
//            modifier = Modifier.size(20.dp),
//            tint = MaterialTheme.colorScheme.onBackground,
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = title.uppercase(),
//            style = MaterialTheme.typography.labelLarge,
//            fontWeight = FontWeight.Black,
//            color = MaterialTheme.colorScheme.onBackground,
//            letterSpacing = 1.sp,
//        )
//    }
//}
//
//@Composable
//fun RecommendedArticleItem(
//    article: Article,
//    onArticleClick: (Int) -> Unit,
//) {
//    Column {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { onArticleClick(article.id) }
//                .padding(horizontal = 24.dp, vertical = 20.dp),
//            verticalAlignment = Alignment.Top,
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    AppImage(
//                        model = article.authorAvatarUrl,
//                        contentDescription = null,
//                        modifier = Modifier.size(20.dp).clip(CircleShape),
//                        shape = CircleShape,
//                    )
//                    Spacer(modifier = Modifier.width(8.dp))
//                    Text(
//                        text = article.authorName,
//                        style = MaterialTheme.typography.labelSmall,
//                        color = MaterialTheme.colorScheme.onSurface,
//                        fontWeight = FontWeight.Medium,
//                    )
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = article.title,
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    lineHeight = 24.sp,
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = article.content,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis,
//                    lineHeight = 20.sp,
//                )
//                Spacer(modifier = Modifier.height(12.dp))
//                Text(
//                    text = "${article.publishedDate} • ${article.readingTimeMinutes} min read",
//                    style = MaterialTheme.typography.labelSmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                )
//            }
//
//            Spacer(modifier = Modifier.width(16.dp))
//
//            AppImage(
//                model = article.thumbnailUrl,
//                contentDescription = null,
//                modifier = Modifier.size(80.dp),
//                shape = MaterialTheme.shapes.small,
//            )
//        }
//        HorizontalDivider(
//            modifier = Modifier.padding(horizontal = 24.dp),
//            thickness = 0.5.dp,
//            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
//        )
//    }
//}
//
//@Preview
//@Composable
//private fun HomeScreenPreview() {
//    MaterialTheme {
//        listOf(
//            Article(
//                id = 1,
//                title = "The Future of Android Development",
//                content = "",
//                authorName = "Android Team",
//                category = "Technology",
//                publishedDate = "May 15",
//                readingTimeMinutes = 5,
//            ),
//            Article(
//                id = 2,
//                title = "Design Systems at Scale",
//                content = "",
//                authorName = "Design Guru",
//                category = "Design",
//                publishedDate = "May 14",
//                readingTimeMinutes = 10,
//            ),
//            Article(
//                id = 3,
//                title = "Kotlin Multiplatform in 2024",
//                content = "",
//                authorName = "JetBrains",
//                category = "Dev",
//                publishedDate = "May 13",
//                readingTimeMinutes = 7,
//            ),
//        )
//        listOf(
//            Article(
//                id = 10,
//                title = "How to build a Medium-style app with Compose",
//                content = "In this article, we explore the design principles and technical implementation of a modern reading application...",
//                authorName = "Gemini CLI",
//                category = "Tutorial",
//                publishedDate = "May 16",
//                readingTimeMinutes = 12,
//            ),
//            Article(
//                id = 11,
//                title = "Why Whitespace Matters",
//                content = "Clean layouts are more than just an aesthetic choice. They improve readability and user retention...",
//                authorName = "UI Expert",
//                category = "Design",
//                publishedDate = "May 15",
//                readingTimeMinutes = 4,
//            ),
//            Article(
//                id = 12,
//                title = "Why Whitespace Matters",
//                content = "Clean layouts are more than just an aesthetic choice. They improve readability and user retention...",
//                authorName = "UI Expert",
//                category = "Design",
//                publishedDate = "May 15",
//                readingTimeMinutes = 4,
//            ),
//
//        )
//    }
//}
