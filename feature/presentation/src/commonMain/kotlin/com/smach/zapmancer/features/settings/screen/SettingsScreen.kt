package com.smach.zapmancer.features.settings.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CorporateFare
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smach.zapmancer.features.common.adaptive.LocalWindowLayout
import com.smach.zapmancer.features.common.adaptive.WindowLayout
import com.smach.zapmancer.features.common.adaptive.rememberWindowLayout
import com.smach.zapmancer.features.common.components.UserAvatar
import com.smach.zapmancer.features.common.components.ZapmancerTopBar
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.features.settings.state.SettingsUiState
import com.smach.zapmancer.features.settings.viewmodel.SettingsEffect
import com.smach.zapmancer.features.settings.viewmodel.SettingsEvent
import com.smach.zapmancer.features.settings.viewmodel.SettingsViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val windowLayout = rememberWindowLayout()

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                SettingsEffect.NavigateBack -> onBackClick()
                SettingsEffect.NavigateToLogin -> onLogoutClick()
            }
        }
    }

    CompositionLocalProvider(LocalWindowLayout provides windowLayout) {
        Scaffold(
            topBar = {
                ZapmancerTopBar(
                    title = "Zapmancer",
                    showBackButton = true,
                    onBackClick = { viewModel.onEvent(SettingsEvent.BackClicked) },
                    actions = {
                        UserAvatar(imageUrl = null, size = 32.dp, modifier = Modifier.padding(end = 12.dp))
                    },
                    containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                    drawBottomBorder = true,
                )
            },
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets(0),
        ) { padding ->
            SettingsContent(
                paddingValues = padding,
                uiState = uiState,
                onToggleTwoFactor = { viewModel.onEvent(SettingsEvent.ToggleTwoFactor(it)) },
                onToggleDarkMode = { viewModel.onEvent(SettingsEvent.ToggleDarkMode(it)) },
                onToggleNotifications = { viewModel.onEvent(SettingsEvent.ToggleEmailNotifications(it)) },
                onToggleClientMode = { viewModel.onEvent(SettingsEvent.ToggleClientMode(it)) },
                onLogout = { viewModel.onEvent(SettingsEvent.Logout) },
            )
        }
    }
}

@Composable
fun SettingsContent(
    paddingValues: PaddingValues,
    uiState: SettingsUiState,
    onToggleTwoFactor: (Boolean) -> Unit,
    onToggleDarkMode: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit,
    onToggleClientMode: (Boolean) -> Unit,
    onLogout: () -> Unit,
) {
    val windowLayout = LocalWindowLayout.current
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.TopCenter) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = windowLayout.contentMaxWidthDp.dp)
                .padding(horizontal = windowLayout.screenHorizontalPaddingDp.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 48.dp),
        ) {
            item {
                Column {
                    Text("Settings", style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
                    Text("Manage your account preferences and security protocols.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp))
                }
            }

            item {
                SettingsSection(title = "Account", icon = Icons.Outlined.AccountCircle) {
                    SettingsItem(title = "Email Address", subtitle = uiState.settings?.email ?: "", actionIcon = Icons.Outlined.Edit)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    SettingsItem(title = "Organization", subtitle = uiState.settings?.organization ?: "", actionIcon = Icons.Outlined.CorporateFare)
                }
            }

            item {
                SettingsSection(title = "Security", icon = Icons.Outlined.Security) {
                    SettingsToggleItem(title = "Two-Factor Authentication", description = "Add an extra layer of security to your account.", checked = uiState.settings?.isTwoFactorEnabled ?: false, onCheckedChange = onToggleTwoFactor)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    SettingsItem(
                        title = "Change Password",
                        subtitle = "Last changed 4 months ago",
                        actionContent = {
                            Button(
                                onClick = {},
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = MaterialTheme.colorScheme.primary),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                                shape = RoundedCornerShape(4.dp),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp),
                            ) {
                                Text("Update", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                            }
                        },
                    )
                }
            }

            item {
                SettingsSection(title = "Preferences", icon = Icons.Outlined.Tune) {
                    SettingsToggleItem(title = "Dark Mode", description = "Switch between light and dark interface themes.", checked = uiState.isDarkModeEnabled, onCheckedChange = onToggleDarkMode)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    SettingsToggleItem(title = "Email Notifications", description = "Receive weekly performance reports and alerts.", checked = uiState.settings?.isEmailNotificationsEnabled ?: false, onCheckedChange = onToggleNotifications)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    SettingsToggleItem(title = "Client Mode", description = "Toggle to switch interface focus to hiring and project posting.", checked = uiState.settings?.isClientModeEnabled ?: false, onCheckedChange = onToggleClientMode)
                }
            }

            item {
                Surface(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Logout", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
                            Text("Session termination will revoke all active access tokens.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Button(
                            onClick = onLogout,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(12.dp)),
                        ) {
                            Text("Sign Out", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalAlignment = Alignment.Start) {
                    Text(uiState.settings?.version ?: "", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), fontWeight = FontWeight.Normal)
                    Text("© 2024 Zapmancer. All systems operational.", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}

@Composable
fun SettingsSection(title: String, icon: ImageVector, content: @Composable ColumnScope.() -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().shadow(1.dp, RoundedCornerShape(8.dp)),
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            Column(content = content)
        }
    }
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    actionIcon: ImageVector? = null,
    actionContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    val clickableModifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier
    Row(
        modifier = Modifier.fillMaxWidth().then(clickableModifier).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (actionContent != null) {
            actionContent()
        } else if (actionIcon != null) {
            Icon(actionIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun SettingsToggleItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f).padding(end = 16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.onSurface)
            Text(description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = MaterialTheme.colorScheme.outlineVariant,
            ),
        )
    }
}

@Composable
@Preview
fun SettingsPreview() {
    AppTheme {
        CompositionLocalProvider(LocalWindowLayout provides WindowLayout.Compact) {
            SettingsContent(
                paddingValues = PaddingValues(0.dp),
                uiState = SettingsUiState(),
                onToggleTwoFactor = {},
                onToggleDarkMode = {},
                onToggleNotifications = {},
                onToggleClientMode = {},
                onLogout = {},
            )
        }
    }
}
