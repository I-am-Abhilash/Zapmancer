package com.smach.zapmancer.features.profile.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import com.smach.zapmancer.domain.model.PortfolioItem
import com.smach.zapmancer.domain.model.ProfileReview
import com.smach.zapmancer.features.common.components.AppImage
import com.smach.zapmancer.features.common.components.ErrorState
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.VerticalDivider
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.profile.state.ProfileUiState
import com.smach.zapmancer.features.profile.viewmodel.ProfileEffect
import com.smach.zapmancer.features.profile.viewmodel.ProfileEvent
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ProfileScreen(
    userId: String? = null,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    onEditProfileClick: () -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    showSnackbar: (String) -> Unit = {},
) {
    val viewModel: ProfileViewModel = koinViewModel(parameters = { parametersOf(userId) })
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(ProfileEvent.Refresh)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ProfileEffect.ShowToast -> {
                    showSnackbar(effect.message)
                }

                ProfileEffect.NavigateToSearch -> onSearchClick()

                ProfileEffect.NavigateBack -> onBackClick()

                ProfileEffect.NavigateToEditProfile -> onEditProfileClick()
            }
        }
    }

    ProfileContent(
        state = state,
        onEvent = { viewModel.onEvent(it) },
        onProfileClick = onProfileClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
    onProfileClick: (String) -> Unit = {},
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val isCompact = adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.COMPACT

    Scaffold(
        topBar = {
            ZapmancerTopBar(
                title = "Zapmancer",
                showBackButton = true,
                onBackClick = { onEvent(ProfileEvent.BackClicked) },
                actions = {
                    IconButton(onClick = { onEvent(ProfileEvent.SearchClicked) }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface,
                drawBottomBorder = true,
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.TopCenter) {
            when {
                state.error != null -> {
                    ErrorState(
                        description = state.error,
                        onButtonClick = { onEvent(ProfileEvent.Refresh) },
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .then(if (!isCompact) Modifier.widthIn(max = 800.dp) else Modifier),
                        contentPadding = PaddingValues(bottom = 32.dp),
                    ) {
                        item { IdentityHeader(state, onEvent) }

                        item {
                            ProfileSectionCard(title = "About") {
                                Text(
                                    text = state.about,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 22.sp,
                                )
                            }
                        }

                        item {
                            ProfileSectionCard(title = "Skills") {
                                FlowRow(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    state.skills.forEach { skill ->
                                        SkillChip(skill)
                                    }
                                }
                            }
                        }

                        item {
                            SectionTitleRow(
                                title = "Portfolio",
                                actionText = "See more",
                                onActionClick = { onEvent(ProfileEvent.PortfolioMore) },
                            )
                        }
                        items(state.portfolioItems) { project ->
                            PortfolioCard(item = project)
                        }

                        item {
                            SectionTitleRow(
                                title = "Top Reviews",
                                actionText = "See all",
                                onActionClick = { onEvent(ProfileEvent.ReviewMore) },
                            )
                        }
                        items(state.reviews) { review ->
                            ReviewCard(review = review, onProfileClick = onProfileClick)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OnMoreButton(onClick: () -> Unit, text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(50),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true)
                .copy(
                    width = 1.dp,
                    brush = SolidColor(
                        MaterialTheme.colorScheme.outline.copy(
                            alpha = 0.5f,
                        ),
                    ),
                ),
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun IdentityHeader(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            UserAvatar(
                imageUrl = state.avatarUrl,
                size = 104.dp,
                borderWidth = 2.dp,
                borderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = state.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = state.role,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(16.dp))

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    8.dp,
                    Alignment.CenterHorizontally,
                ),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoChip(
                    Icons.Default.LocationOn,
                    state.location,
                )

                InfoChip(
                    Icons.Default.MilitaryTech,
                    state.ranking,
                )

                if (state.isTopRated) {
                    InfoChip(
                        Icons.Default.Verified,
                        "Top Rated",
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                StatItem(
                    value = state.projectsCount.toString(),
                    label = "Projects",
                )

                VerticalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                )

                StatItem(
                    value = state.rating.toString(),
                    label = "Rating",
                )

                VerticalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                )

                StatItem(
                    value = state.experience,
                    label = "Exp",
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Deduplicated CTA button — only label & event differ
            val ctaLabel = if (!state.isOwnProfile) "Hire Me" else "Edit Profile"
            val ctaEvent = if (!state.isOwnProfile) ProfileEvent.HireMe else ProfileEvent.EditProfileClicked

            Button(
                onClick = { onEvent(ctaEvent) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.surface,
                ),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text(
                    text = ctaLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}

@Composable
fun ProfileSectionCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 16.dp),
            )
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
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 1.sp,
        )
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
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp),
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = textColor,
            )
        }
    }
}

@Composable
fun SectionTitleRow(
    title: String,
    actionText: String = "",
    onActionClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )
        if (actionText.isNotEmpty()) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(onClick = onActionClick),
            )
        }
    }
}

@Composable
fun PortfolioCard(item: PortfolioItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column {
            AppImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
fun ReviewCard(review: ProfileReview, onProfileClick: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            val drawLineColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    UserAvatar(
                        imageUrl = review.authorAvatarUrl,
                        size = 48.dp,
                        borderWidth = 2.dp,
                        borderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                        modifier = Modifier.clickable { onProfileClick(review.authorId) },
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.clickable { onProfileClick(review.authorId) }) {
                        Text(
                            text = review.authorName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = review.authorRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }

                // Accessible star rating: semantic description on the Row
                Row(
                    modifier = Modifier.semantics {
                        contentDescription = "${review.rating} out of 5 stars"
                    },
                ) {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp),
                        )
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
                        drawLine(
                            color = drawLineColor,
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx(),
                        )
                    }
                    .padding(start = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        ProfileContent(
            state = ProfileUiState(),
            onEvent = {},
            onProfileClick = {},
        )
    }
}
