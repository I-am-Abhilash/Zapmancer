package com.smach.zapmancer.presentation.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val LocalDrawerController =
    staticCompositionLocalOf<DrawerController> {
        error("DrawerController not provided")
    }

class DrawerController(
    private val drawerState: DrawerState,
    private val scope: CoroutineScope,
) {
    fun open() {
        scope.launch {
            drawerState.open()
        }
    }

    fun close() {
        scope.launch {
            drawerState.close()
        }
    }
}

@Composable
fun AppDrawerContent(
    isClientMode: Boolean,
    onNavigateToProfile: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onCreateProjectClick: () -> Unit,
    onNavigateToProposal: () -> Unit,
    closeDrawer: () -> Unit,
) {
    ModalDrawerSheet(
        drawerContainerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.width(300.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
        ) {
            Text(
                text = "Zapmancer",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )

            Spacer(Modifier.height(24.dp))

            DrawerItem(
                icon = Icons.Default.Work,
                label = "Home Dashboard",
                onClick = closeDrawer,
            )

            if (isClientMode) {
                DrawerItem(
                    icon = Icons.Default.Payments,
                    label = "Post a Project",
                    onClick = {
                        closeDrawer()
                        onCreateProjectClick()
                    },
                )

                DrawerItem(
                    icon = Icons.Default.Star,
                    label = "Review Project Bids",
                    onClick = {
                        closeDrawer()
                        onNavigateToProposal()
                    },
                )
            } else {
                DrawerItem(
                    icon = Icons.Default.Payments,
                    label = "Create Proposal",
                    onClick = {
                        closeDrawer()
                        onNavigateToProposal()
                    },
                )

                DrawerItem(
                    icon = Icons.Default.Star,
                    label = "My Profile",
                    onClick = {
                        closeDrawer()
                        onNavigateToProfile()
                    },
                )
            }

            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = {
                    closeDrawer()
                    onNavigateToSettings()
                },
            )

            Spacer(Modifier.weight(1f))

            Text(
                text = "1.0.0",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

@Composable
private fun DrawerItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp),
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
