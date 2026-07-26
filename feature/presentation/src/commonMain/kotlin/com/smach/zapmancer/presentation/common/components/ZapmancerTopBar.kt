package com.smach.zapmancer.presentation.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ─── Primary Nav Bar ──────────────────────────────────────────────────────────
// Fully design-agnostic: every token comes from MaterialTheme.
//
//   containerColor → colorScheme.surface  (NikeCanvas in Nike, any other surface in other themes)
//   title color    → colorScheme.onSurface
//   icon tint      → colorScheme.onSurface
//   bottom border  → colorScheme.outline (NikeHairline in Nike)
//
// Swapping the 4 theme files will fully restyle this component.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZapmancerTopBar(
    title: String = "Zapmancer",
    titleContent: (@Composable () -> Unit)? = null,
    showBackButton: Boolean = false,
    showMenuButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onMenuClick: () -> Unit = {},
    actions: (@Composable RowScope.() -> Unit)? = null,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    drawBottomBorder: Boolean = true,
) {
    // Capture theme tokens outside the drawBehind lambda (no Composable context inside)
    val borderColor = MaterialTheme.colorScheme.outline
    val contentColor = MaterialTheme.colorScheme.onSurface

    TopAppBar(
        title = {
            if (titleContent != null) {
                titleContent()
            } else {
                Text(
                    text = title,
                    color = contentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 0.sp,
                )
            }
        },
        navigationIcon = {
            when {
                showBackButton -> IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = contentColor,
                    )
                }

                showMenuButton -> IconButton(onClick = onMenuClick) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = contentColor,
                    )
                }
            }
        },
        actions = { if (actions != null) actions() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            scrolledContainerColor = containerColor,
        ),
        modifier = if (drawBottomBorder) {
            Modifier.drawBehind {
                drawLine(
                    color = borderColor,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 1.dp.toPx(),
                )
            }
        } else {
            Modifier
        },
    )
}
