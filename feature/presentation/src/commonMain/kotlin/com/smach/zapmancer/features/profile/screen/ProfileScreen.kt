package com.smach.zapmancer.features.profile.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.PortfolioItem
import com.smach.zapmancer.domain.model.ProfileReview
import com.smach.zapmancer.features.common.adaptive.AdaptiveScaffold
import com.smach.zapmancer.features.common.adaptive.LocalWindowLayout
import com.smach.zapmancer.features.common.adaptive.NavDestination
import com.smach.zapmancer.features.common.adaptive.WindowLayout
import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
import com.smach.zapmancer.features.common.components.AppImage
import com.smach.zapmancer.features.common.components.LocalDrawerController
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.profile.state.ProfileUiState
import com.smach.zapmancer.features.profile.viewmodel.ProfileEffect
import com.smach.zapmancer.features.profile.viewmodel.ProfileEvent
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import com.smach.zapmancer.features.projects.screen.VerticalDivider
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileScreen(
    userId: String? = null,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    onProfileClick: (String) -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToProjects: () -> Unit = {},
    onNavigateToMessages: () -> Unit = {},
    onNavigateToNotifications: () -> Unit = {},
    currentRoute: String = NavDestination.Profile.route,
    showSnackbar: (String) -> Unit = {},
) {
    val viewModel: ProfileViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val state by viewModel.uiState.collectAsState()
    val drawerController = LocalDrawerController.current
    val windowLayout = rememberWindowLayout()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowToast -> showSnackbar(effect.message)
            }
        }
    }

    CompositionLocalProvider(LocalWindowLayout provides windowLayout) {
        AdaptiveScaffold(
            currentRoute = currentRoute,
            onNavigate = { dest ->
                when (dest) {
                    NavDestination.Home -> onNavigateToHome()
                    NavDestination.Projects -> onNavigateToProjects()
                    NavDestination.Messages -> onNavigateToMessages()
                    NavDestination.Notifications -> onNavigateToNotifications()
                    NavDestination.Profile -> Unit
                }
            },
            title = {
                ZapmancerTopBar(
                    title = "Zapmancer",
                    showBackButton = windowLayout.isCompact && userId != null,
                    onBackClick = onBackClick,
                    actions = {
                        IconButton(onClick = { onSearchClick() }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    drawBottomBorder = true,
                )
            },
        ) { padding ->
            ProfileContent(
                paddingValues = padding,
                state = state,
                onEvent = { viewModel.onEvent(it) },
                onProfileClick = onProfileClick,
            )
        }
    }
}

@Composable
fun ProfileContent(
    paddingValues: PaddingValues,
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    onProfileClick: (String) -> Unit = {},
) {
    val windowLayout = LocalWindowLayout.current
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = windowLayout.contentMaxWidthDp.dp),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            item { IdentityHeader(state, onEvent) }
            item {
                ProfileSectionCard(title = "About") {
                    Text(state.about, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, lineHeight = 22.sp)
                }
            }
            item {
                ProfileSectionCard(title = "Skills") {
                    FlowRow(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        state.skills.forEach { SkillChip(it) }
                    }
                }
            }
            item { SectionTitleRow(title = "Portfolio") }
            items(state.portfolioItems) { PortfolioCard(it) }
            item { OnMoreButton(text = "See more", onClick = { onEvent(ProfileEvent.PortfolioMore) }) }
            item { SectionTitleRow(title = "Top Reviews") }
            items(state.reviews) { ReviewCard(review = it, onProfileClick = onProfileClick) }
            item { OnMoreButton(text = "See more reviews", onClick = { onEvent(ProfileEvent.ReviewMore) }) }
        }
    }
}

@Composable
fun OnMoreButton(onClick: () -> Unit, text: String) {
    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp), contentAlignment = Alignment.Center) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                .copy(width = 1.dp, brush = SolidColor(MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))),
        ) {
            Text(text = text, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IdentityHeader(state: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    val windowLayout = LocalWindowLayout.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(if (windowLayout.isCompact) 16.dp else 24.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.widthIn(max = 220.dp)) {
                UserAvatar(imageUrl = state.avatarUrl, size = 104.dp, borderWidth = 2.dp, borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = state.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = state.role, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column(modifier = Modifier.weight(1f)) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    InfoChip(Icons.Default.LocationOn, state.location)
                    InfoChip(Icons.Default.MilitaryTech, state.ranking)
                    if (state.isTopRated) InfoChip(Icons.Default.Verified, "Top Rated")
                }
                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                Row(
                    modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(vertical = 20.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    StatItem(state.projectsCount.toString(), "Projects")
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    StatItem(state.rating.toString(), "Rating")
                    VerticalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    StatItem(state.experience, "Exp")
                }
                if (!state.isOwnProfile) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { onEvent(ProfileEvent.HireMe) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(999.dp),
                    ) {
                        Text("Hire Me", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileSectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(bottom = 16.dp))
            content()
        }
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
    ) {
        Text(
            text = skill.uppercase(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 0.5.sp,
        )
    }
}

@Composable
fun StatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
        Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, letterSpacing = 1.sp)
    }
}

@Composable
fun InfoChip(
    icon: ImageVector,
    text: String,
    bgColor: Color = MaterialTheme.colorScheme.surface,
    textColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(999.dp),
        border = BorderStroke(0.5.dp, textColor.copy(alpha = 0.2f)),
    ) {
        Row(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = textColor, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}

@Composable
fun SectionTitleRow(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(title, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
fun PortfolioCard(item: PortfolioItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { }
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            AppImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxWidth().aspectRatio(16 / 9f),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(item.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                Text(item.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ReviewCard(review: ProfileReview, onProfileClick: (String) -> Unit = {}) {
    val drawLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(imageUrl = review.authorAvatarUrl, size = 48.dp, borderWidth = 2.dp, borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), modifier = Modifier.clickable { onProfileClick(review.authorId) })
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.clickable { onProfileClick(review.authorId) }) {
                        Text(review.authorName, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Text(review.authorRole, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Row {
                    repeat(review.rating) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "\"${review.content}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .drawBehind {
                        drawLine(color = drawLineColor, start = Offset(0f, 0f), end = Offset(0f, size.height), strokeWidth = 4.dp.toPx())
                    }
                    .padding(start = 16.dp),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    maxItemsInEachRow: Int = Int.MAX_VALUE,
    content: @Composable FlowRowScope.() -> Unit,
) {
    FlowRow(modifier = modifier, horizontalArrangement = horizontalArrangement, verticalArrangement = verticalArrangement, maxItemsInEachRow = maxItemsInEachRow, content = content)
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        CompositionLocalProvider(LocalWindowLayout provides WindowLayout.Compact) {
            ProfileContent(
                paddingValues = PaddingValues(0.dp),
                state = ProfileUiState(),
                onEvent = {},
                onProfileClick = {},
            )
        }
    }
}
