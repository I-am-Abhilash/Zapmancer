package com.smach.zapmancer.features.home.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.domain.model.ActivityStatus
import com.smach.zapmancer.features.alerts.screen.drawAccentLine
import com.smach.zapmancer.features.common.theme.ZapGold
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.home.state.RecentActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onCreateProjectClick: () -> Unit = {}
) {
    val drawLineColor = MaterialTheme.colorScheme.outlineVariant

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Bolt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                        Text(
                            "Zapmancer",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "Menu", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        // Avatar placeholder
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.drawBehind {
                    drawLine(
                        color = drawLineColor,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        HomeContent(
            state = state,
            onCreateProjectClick = onCreateProjectClick,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun HomeContent(
    state: HomeUiState,
    onCreateProjectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Hero Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    "Welcome back, ${state.userName}".uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 1.sp
                )
                Text(
                    "Start your Journey.",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Total Earnings",
                value = state.totalEarnings,
                accentColor = MaterialTheme.colorScheme.primary,
                icon = Icons.Default.Payments,
                growth = state.earningsGrowth
            )
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                title = "Current Projects",
                value = state.activeProjectsCount.toString(),
                accentColor = ZapGold,
                icon = Icons.Default.Work,
                secondaryValue = "/ ${state.totalCapacity} capacity"
            )
            StatCard(
                modifier = Modifier.fillMaxWidth(),
                title = "System Rating",
                value = state.systemRating.toString(),
                accentColor = MaterialTheme.colorScheme.secondary,
                icon = Icons.Default.Star,
                isRating = true
            )
        }
        Button(
            onClick = onCreateProjectClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
            ),
            shape = RoundedCornerShape(999.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            Text("Create Project", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Activity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = {}) {
                    Text("Export CSV", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    state.recentActivities.forEach { activity ->
                        ActivityRow(activity)
                    }
                }
            }
        }
        Card(
            modifier = modifier
                .fillMaxSize()
                .height(160.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4 / 3f)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface)
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f)
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = ZapGold,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            "New Feature",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Quantum Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Real-time predictive modeling is now available in your workspace.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Launch Dashboard", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    accentColor: Color,
    icon: ImageVector,
    growth: String? = null,
    secondaryValue: String? = null,
    isRating: Boolean = false
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier.drawAccentLine(accentColor).padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                            .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    if (growth != null) {
                        Surface(
                            color = accentColor,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                growth,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Column {
                    Text(
                        title.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            value,
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (secondaryValue != null) {
                            Text(
                                secondaryValue,
                                modifier = Modifier.padding(bottom = 8.dp),
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (isRating) {
                            Row(modifier = Modifier.padding(bottom = 12.dp)) {
                                repeat(5) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = null,
                                        tint = ZapGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun ActivityRow(
    activity: RecentActivity
) {
    val accentColor = when (activity.status) {
        ActivityStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primary
        ActivityStatus.REVIEWING -> ZapGold
        ActivityStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        ActivityStatus.CRITICAL -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)

    ) {
        Column(
            modifier = Modifier
                .drawAccentLine(accentColor)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                accentColor.copy(alpha = 0.15f),
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                1.dp,
                                accentColor.copy(alpha = 0.4f),
                                RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = activity.categoryTag,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                    }

                    Column {
                        Text(
                            text = activity.projectName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

//                        Text(
//                            text = activity.category,
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                        )
                    }
                }

                Text(
                    text = activity.value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = activity.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Surface(
                    color = accentColor,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = activity.status.name
                            .replace('_', ' '),
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp
                        ),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Preview(
    device = PIXEL_9_PRO
)
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            state = HomeUiState(
                userName = "Alex",
                recentActivities = listOf(
                    RecentActivity(
                        "1",
                        "Neural Engine Optimizer",
                        "Infrastructure",
                        "AI",
                        ActivityStatus.IN_PROGRESS,
                        "2h ago",
                        "$12,400.00"
                    ),
                    RecentActivity(
                        "2",
                        "Dashboard Redesign",
                        "Visual Design",
                        "UX",
                        ActivityStatus.REVIEWING,
                        "Yesterday",
                        "$4,200.00"
                    ),
                    RecentActivity(
                        "3",
                        "SQL Latency Patch",
                        "Backend",
                        "DB",
                        ActivityStatus.COMPLETED,
                        "Oct 24",
                        "$8,150.00"
                    ),
                    RecentActivity(
                        "4",
                        "Security Audit",
                        "Compliance",
                        "SY",
                        ActivityStatus.CRITICAL,
                        "Oct 22",
                        "$15,000.00"
                    )
                )
            )
        )
    }
}
