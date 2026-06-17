package com.smach.zapmancer.features.profile.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.common.components.AppImage
import com.smach.zapmancer.features.profile.state.PortfolioItem
import com.smach.zapmancer.features.profile.state.ProfileReview
import com.smach.zapmancer.features.profile.state.ProfileUiState
import com.smach.zapmancer.features.profile.viewmodel.ProfileEvent
import com.smach.zapmancer.features.profile.viewmodel.ProfileViewModel

// Flip7 Palette
private val ZapTeal = Color(0xFF2BA8A2)
private val ZapTealDark = Color(0xFF1D736F)
private val ZapBg = Color(0xFFF5FAF9)
private val ZapSurface = Color(0xFFFFFFFF)
private val ZapOnSurface = Color(0xFF131B2E)
private val ZapOnSurfaceVariant = Color(0xFF434655)
private val ZapOutlineVariant = Color(0xFFC3C6D7)
private val ZapGold = Color(0xFFFFD700)

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileContent(
        state = uiState,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    state: ProfileUiState,
    onEvent: (ProfileEvent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Zapmancer",
                        color = ZapTeal,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle Back */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Handle Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = ZapTeal)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ZapSurface
                )
            )
        },
        containerColor = ZapBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item { IdentityHeader(state, onEvent) }

            item {
                ProfileSectionCard("About") {
                    Text(
                        text = state.about,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ZapOnSurfaceVariant,
                        lineHeight = 22.sp
                    )
                }
            }

            item {
                ProfileSectionCard("Skills") {
                    FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.skills.forEach { skill ->
                        SkillChip(skill)
                    }
                }
                }
            }

            // Portfolio Section
            item { 
                SectionTitleRow("Portfolio", "View all projects")
            }
            items(state.portfolioItems) { project ->
                PortfolioCard(project)
            }

            // Reviews Section
            item { 
                SectionTitleRow("Top Reviews", "")
            }
            items(state.reviews) { review ->
                ReviewCard(review)
            }
        }
    }
}

@Composable
fun IdentityHeader(state: ProfileUiState, onEvent: (ProfileEvent) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
             //Banner Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(ZapTeal, ZapTealDark)
                        )
                    )
            )

            // Profile Info Content
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 24.dp)
                    .offset(y = (-48).dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar
                AppImage(
                    model = state.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(128.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .border(4.dp, ZapSurface, RoundedCornerShape(16.dp))
                        .background(ZapSurface)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = state.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = state.role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ZapTeal,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, ZapOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(0.dp))
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem(state.projectsCount.toString(), "Projects")
                    StatItem(state.rating.toString(), "Rating")
                    StatItem(state.experience, "Exp")
                }
                Spacer(modifier = Modifier.height(24.dp))


                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    InfoChip(Icons.Default.LocationOn, state.location)
                    Spacer(modifier = Modifier.width(8.dp))
                    InfoChip(Icons.Default.MilitaryTech, state.ranking, Color(0xFFE8E8E8), Color(0xFF717171))
                    Spacer(modifier = Modifier.width(8.dp))
                    if (state.isTopRated) {
                        InfoChip(Icons.Default.Verified, "Top Rated", Color(0xFFFFF5F2), Color(0xFFFF7F50))
                    }
                }



                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { onEvent(ProfileEvent.HireMe) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .shadow(4.dp, CircleShape),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ZapGold
                    ),
                    shape = CircleShape
                ) {
                    Text(
                        "Hire Julian",
                        color = Color(0xFF1A1A1A),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
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
            .border(1.dp, ZapOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun SkillChip(skill: String) {
    Surface(
        color = ZapTeal.copy(alpha = 0.1f),
        shape = RoundedCornerShape(999.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapTeal.copy(alpha = 0.2f))
    ) {
        Text(
            text = skill.uppercase(),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = ZapTeal,
            letterSpacing = 0.5.sp
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
            color = ZapOnSurface
        )
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = ZapOnSurfaceVariant,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun InfoChip(icon: ImageVector, text: String, bgColor: Color = ZapBg, textColor: Color = ZapOnSurfaceVariant) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(999.dp),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, textColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

@Composable
fun SectionTitleRow(title: String, actionText: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = ZapOnSurface
        )
        if (actionText.isNotEmpty()) {
            Text(
                text = actionText,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = ZapTeal,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Composable
fun PortfolioCard(item: PortfolioItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clickable { }
            .drawBehind {
                val strokeWidth = 4.dp.toPx()
                drawLine(
                    color = ZapTeal,
                    start = Offset(0f, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            },
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column {
            AppImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface
                )
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = ZapOnSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ReviewCard(review: ProfileReview) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(1.dp, ZapOutlineVariant.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AppImage(
                        model = review.authorAvatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(2.dp, ZapTeal.copy(alpha = 0.2f), CircleShape)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = review.authorName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = ZapOnSurface
                        )
                        Text(
                            text = review.authorRole,
                            style = MaterialTheme.typography.labelSmall,
                            color = ZapOnSurfaceVariant
                        )
                    }
                }
                
                Row {
                    repeat(review.rating) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = ZapTeal,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "\"${review.content}\"",
                style = MaterialTheme.typography.bodyMedium,
                color = ZapOnSurfaceVariant,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = ZapTeal.copy(alpha = 0.2f),
                            start = Offset(0f, 0f),
                            end = Offset(0f, size.height),
                            strokeWidth = 4.dp.toPx()
                        )
                    }
                    .padding(start = 16.dp)
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
    content: @Composable FlowRowScope.() -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        maxItemsInEachRow = maxItemsInEachRow,
        content = content
    )
}

@Preview
@Composable
fun ProfileScreenPreview(){
    MaterialTheme {
        ProfileContent(state = ProfileUiState(), onEvent = {})
    }
}
