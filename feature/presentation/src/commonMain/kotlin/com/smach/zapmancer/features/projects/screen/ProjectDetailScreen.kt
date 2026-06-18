package com.smach.zapmancer.features.projects.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.projects.state.ProjectDetailUiState

private val ZapTeal = Color(0xFF2BA8A2)
private val ZapBg = Color(0xFFF5FAF9)
private val ZapSurface = Color(0xFFFFFFFF)
private val ZapOnSurface = Color(0xFF171D1C)
private val ZapOnSurfaceVariant = Color(0xFF3F4948)
private val ZapOutlineVariant = Color(0xFFDEE4E2)
private val ZapGold = Color(0xFFA89000)
private val ZapGoldContainer = Color(0xFFFFDF00)
private val ZapOnGoldContainer = Color(0xFF241D00)

@Composable
fun ProjectDetailScreen(
    state: ProjectDetailUiState = ProjectDetailUiState(),
    onBackClick: () -> Unit = {}
) {
    ProjectDetailContent(state = state, onBackClick = onBackClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectDetailContent(
    state: ProjectDetailUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Zapmancer",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = ZapOnSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(ZapOutlineVariant)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ZapSurface
                ),
                modifier = Modifier.border(0.5.dp, ZapOutlineVariant)
            )
        },
        containerColor = ZapBg
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { ProjectHeaderSection(state) }
            item { ProjectScopeSection(state) }
            item { RequiredSkillsSection(state) }
            item { SummaryStatsSection(state) }
            item { ClientSummarySection(state) }
            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}

@Composable
fun ProjectHeaderSection(state: ProjectDetailUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Code,
                    contentDescription = null,
                    tint = ZapTeal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    state.category.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = ZapTeal,
                    letterSpacing = 1.sp
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                state.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = ZapOnSurface,
                lineHeight = 32.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(Icons.Default.Schedule, "Posted ${state.postedTime}")
                InfoItem(Icons.Default.LocationOn, state.location)

                if (state.isPaymentVerified) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Verified,
                            contentDescription = null,
                            tint = ZapTeal,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Payment Verified",
                            style = MaterialTheme.typography.bodySmall,
                            color = ZapTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            tint = ZapOnSurfaceVariant,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = ZapOnSurfaceVariant
        )
    }
}

@Composable
fun ProjectScopeSection(state: ProjectDetailUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Project Scope",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                state.description,
                style = MaterialTheme.typography.bodyLarge,
                color = ZapOnSurfaceVariant,
                lineHeight = 24.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(color = ZapOutlineVariant)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Key Deliverables",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            state.deliverables.forEach { deliverable ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = ZapTeal,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        deliverable,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ZapOnSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RequiredSkillsSection(state: ProjectDetailUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                "Required Skills",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurface
            )
            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                state.skills.forEach { skill ->
                    Surface(
                        color = ZapTeal.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(999.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ZapTeal.copy(alpha = 0.2f))
                    ) {
                        Text(
                            skill,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = ZapTeal
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryStatsSection(state: ProjectDetailUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(ZapGoldContainer, ZapGold)
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Column {
                    Text(
                        "TOTAL BUDGET",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnGoldContainer.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        state.budgetRange,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = ZapOnGoldContainer
                    )
                    Text(
                        state.projectType,
                        style = MaterialTheme.typography.bodySmall,
                        color = ZapOnGoldContainer.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = ZapOnGoldContainer.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))
                Column {
                    Text(
                        "TIMELINE",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnGoldContainer.copy(alpha = 0.8f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        state.timeline,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnGoldContainer
                    )
                    Text(
                        "Est. Start: ${state.estStart}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ZapOnGoldContainer.copy(alpha = 0.9f)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ZapOnGoldContainer,
                        contentColor = ZapGoldContainer
                    ),
                    shape = RoundedCornerShape(999.dp),
                    contentPadding = PaddingValues(vertical = 12.dp)
                ) {
                    Text("Apply Now", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = ZapSurface,
                contentColor = ZapOnSurface
            ),
            shape = RoundedCornerShape(999.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text("Save Project", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}

@Composable
fun ClientSummarySection(state: ProjectDetailUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOutlineVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Client Summary",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = ZapOnSurface
                )
                if (state.isClientActive) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(8.dp),
                            color = ZapTeal,
                            shape = CircleShape
                        ) {}
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            "Active Now",
                            style = MaterialTheme.typography.labelSmall,
                            color = ZapTeal,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(ZapBg)
                        .border(1.dp, ZapOutlineVariant, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    // Placeholder for logo
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        state.clientName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnSurface
                    )
                    Text(
                        "${state.clientIndustry} · ${state.clientLocation}",
                        style = MaterialTheme.typography.bodySmall,
                        color = ZapOnSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, ZapOutlineVariant, RoundedCornerShape(8.dp))
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "PROJECTS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnSurfaceVariant
                    )
                    Text(
                        state.clientProjectsCount.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnSurface
                    )
                }
                VerticalDivider(color = ZapOutlineVariant)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "RATING",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = ZapOnSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            state.clientRating.toString(),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = ZapOnSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            tint = ZapGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "VERIFICATION",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = ZapOnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            VerificationItem("Payment Method Verified", state.isPaymentVerified)
            VerificationItem("Identity Verified", state.isIdentityVerified)
            VerificationItem("Phone Number Verified", state.isPhoneVerified)
        }
    }
}

@Composable
fun VerificationItem(text: String, isVerified: Boolean = false) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = null,
            tint = if (isVerified) ZapTeal else ZapOnSurfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall,
            color = ZapOnSurface
        )
    }
}

@Composable
fun VerticalDivider(color: Color) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(40.dp)
            .background(color)
    )
}

@Preview
@Composable
fun ProjectDetailScreenPreview() {
    MaterialTheme {
        ProjectDetailScreen()
    }
}
