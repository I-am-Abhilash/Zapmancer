package com.smach.zapmancer.features.home.screen

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.home.state.ActivityStatus
import com.smach.zapmancer.features.home.state.HomeUiState
import com.smach.zapmancer.features.home.state.RecentActivity

// Flip7 Palette
private val ZapTeal = Color(0xFF2BA8A2)
private val ZapCoral = Color(0xFFFF6B6B)
private val ZapGold = Color(0xFFFFD93D)
private val ZapBg = Color(0xFFEFF8F7)
private val ZapSurface = Color(0xFFFFFFFF)
private val ZapOnSurface = Color(0xFF1A1C1E)
private val ZapOnSurfaceVariant = Color(0xFF434655)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeUiState,
    onCreateProjectClick: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Bolt, contentDescription = null, tint = ZapTeal, modifier = Modifier.size(28.dp))
                        Text(
                            "Zapmancer",
                            color = ZapTeal,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 24.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Bolt, contentDescription = "Menu", tint = ZapOnSurface)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = ZapOnSurface)
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(2.dp, ZapOnSurface, RoundedCornerShape(8.dp))
                            .background(ZapBg)
                    ) {
                        // Avatar placeholder
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ZapSurface),
                modifier = Modifier.drawBehind {
                    drawLine(
                        color = ZapOnSurface,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            )
        },
        containerColor = ZapBg
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
                    color = ZapTeal,
                    letterSpacing = 1.sp
                )
                Text(
                    "Start your Journey.",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = ZapOnSurface
                )
            }
        }
        Button(
            onClick = onCreateProjectClick,
            colors = ButtonDefaults.buttonColors(containerColor = ZapTeal),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, ZapOnSurface),
            modifier = Modifier.height(48.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Create Project", fontWeight = FontWeight.Bold)
        }

        // Stats Grid
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Total Earnings",
                value = state.totalEarnings,
                accentColor = ZapTeal,
                icon = Icons.Default.Payments,
                growth = state.earningsGrowth
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Current Projects",
                value = state.activeProjectsCount.toString(),
                accentColor = ZapGold,
                icon = Icons.Default.Work,
                secondaryValue = "/ ${state.totalCapacity} capacity"
            )
        }

        StatCard(
            modifier = Modifier.fillMaxWidth(),
            title = "System Rating",
            value = state.systemRating.toString(),
            accentColor = ZapCoral,
            icon = Icons.Default.Star,
            isRating = true
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Column(modifier = Modifier.weight(2f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Recent Activity", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    TextButton(onClick = {}) {
                        Text("Export CSV", color = ZapTeal, fontWeight = FontWeight.Bold)
                    }
                }
                
                Card(
                    colors = CardDefaults.cardColors(containerColor = ZapSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZapOnSurface),
                    shape = RoundedCornerShape(0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ZapBg)
                                .drawBehind {
                                    drawLine(
                                        color = ZapOnSurface,
                                        start = Offset(0f, size.height),
                                        end = Offset(size.width, size.height),
                                        strokeWidth = 2.dp.toPx()
                                    )
                                }
                                .padding(12.dp)
                        ) {
                            Text("Project Name", modifier = Modifier.weight(2f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Status", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Date", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Value", modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        
                        state.recentActivities.forEach { activity ->
                            ActivityRow(activity)
                        }
                    }
                }
            }

            // Insights
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                // System Health
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = ZapSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ZapOnSurface),
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("System Health", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.height(16.dp))
                        HealthBar("CPU Usage", state.cpuUsage, ZapTeal)
                        Spacer(Modifier.height(12.dp))
                        HealthBar("Memory Load", state.memoryLoad, ZapCoral)
                        
                        Spacer(Modifier.height(16.dp))
                        HorizontalDivider(thickness = 2.dp, color = ZapOnSurface.copy(alpha = 0.1f))
                        Spacer(Modifier.height(12.dp))
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                PulseIndicator()
                                Text("All Nodes Active", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ZapOnSurfaceVariant)
                            }
                            Text("LAST SYNC: ${state.lastSyncTime}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ZapOnSurfaceVariant)
                        }
                    }
                }

                // Featured Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4 / 3f)
                        .border(1.dp, ZapOnSurface)
                        .background(ZapSurface)
                ) {
                    // Gradient/Pattern Background
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, ZapOnSurface.copy(alpha = 0.05f))
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
                            border = androidx.compose.foundation.BorderStroke(1.dp, ZapOnSurface),
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
                        Text("Quantum Analytics", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text(
                            "Real-time predictive modeling is now available in your workspace.",
                            fontSize = 12.sp,
                            color = ZapOnSurfaceVariant
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = ZapSurface, contentColor = ZapTeal),
                            border = androidx.compose.foundation.BorderStroke(2.dp, ZapOnSurface),
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
            .height(160.dp)
            .drawBehind {
                val strokeWidth = 6.dp.toPx()
                drawLine(
                    color = accentColor,
                    start = Offset(strokeWidth / 2, 0f),
                    end = Offset(strokeWidth / 2, size.height),
                    strokeWidth = strokeWidth
                )
            },
        colors = CardDefaults.cardColors(containerColor = ZapSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, ZapOnSurface),
        shape = RoundedCornerShape(0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(accentColor.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .border(1.dp, accentColor.copy(alpha = 0.4f), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = accentColor, modifier = Modifier.size(20.dp))
                }
                
                if (growth != null) {
                    Surface(
                        color = accentColor,
                        shape = RoundedCornerShape(2.dp)
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
                Text(title.uppercase(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ZapOnSurfaceVariant)
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(value, style = MaterialTheme.typography.displayMedium, fontWeight = FontWeight.ExtraBold, color = ZapOnSurface)
                    if (secondaryValue != null) {
                        Text(secondaryValue, modifier = Modifier.padding(bottom = 8.dp), fontSize = 14.sp, color = ZapOnSurfaceVariant)
                    }
                    if (isRating) {
                        Row(modifier = Modifier.padding(bottom = 12.dp)) {
                            repeat(5) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = ZapGold, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityRow(activity: RecentActivity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, ZapOnSurface, RoundedCornerShape(4.dp))
                    .background(ZapTeal.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(activity.categoryTag, fontWeight = FontWeight.Bold, color = ZapTeal)
            }
            Column {
                Text(activity.projectName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(activity.category, fontSize = 12.sp, color = ZapOnSurfaceVariant)
            }
        }
        
        Box(modifier = Modifier.weight(1f)) {
            Surface(
                color = when(activity.status) {
                    ActivityStatus.IN_PROGRESS -> ZapTeal
                    ActivityStatus.REVIEWING -> ZapGold
                    ActivityStatus.COMPLETED -> ZapSurface
                    ActivityStatus.CRITICAL -> ZapCoral
                },
                contentColor = if (activity.status == ActivityStatus.COMPLETED) ZapTeal else Color.White,
                border = androidx.compose.foundation.BorderStroke(2.dp, ZapOnSurface),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = when(activity.status) {
                        ActivityStatus.IN_PROGRESS -> "In Progress"
                        ActivityStatus.REVIEWING -> "Reviewing"
                        ActivityStatus.COMPLETED -> "Completed"
                        ActivityStatus.CRITICAL -> "Critical"
                    }.uppercase(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Text(activity.date, modifier = Modifier.weight(1f), fontSize = 13.sp, fontWeight = FontWeight.Medium, color = ZapOnSurfaceVariant)
        Text(activity.value, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun HealthBar(label: String, progress: Float, color: Color) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ZapOnSurfaceVariant)
            Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = ZapOnSurface)
        }
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .border(1.dp, ZapOnSurface, RoundedCornerShape(2.dp))
                .background(ZapBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
                    .background(color)
                    .drawBehind {
                        drawLine(
                            color = ZapOnSurface,
                            start = Offset(size.width, 0f),
                            end = Offset(size.width, size.height),
                            strokeWidth = 1.dp.toPx()
                        )
                    }
            )
        }
    }
}

@Composable
fun PulseIndicator() {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = androidx.compose.animation.core.RepeatMode.Reverse
        )
    )
    
    Box(
        modifier = Modifier
            .size(12.dp)
            .clip(CircleShape)
            .background(ZapTeal.copy(alpha = alpha))
            .border(1.dp, ZapOnSurface, CircleShape)
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    MaterialTheme {
        HomeScreen(
            state = HomeUiState(
                userName = "Alex",
                recentActivities = listOf(
                    RecentActivity("1", "Neural Engine Optimizer", "Infrastructure", "AI", ActivityStatus.IN_PROGRESS, "2h ago", "$12,400.00"),
                    RecentActivity("2", "Dashboard Redesign", "Visual Design", "UX", ActivityStatus.REVIEWING, "Yesterday", "$4,200.00"),
                    RecentActivity("3", "SQL Latency Patch", "Backend", "DB", ActivityStatus.COMPLETED, "Oct 24", "$8,150.00"),
                    RecentActivity("4", "Security Audit", "Compliance", "SY", ActivityStatus.CRITICAL, "Oct 22", "$15,000.00")
                )
            )
        )
    }
}
