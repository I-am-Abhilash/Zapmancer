package com.smach.zapmancer.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun UserAvatar(
    imageUrl: String?,
    size: Dp = 32.dp,
    shape: Shape = CircleShape,
    isOnline: Boolean = false,
    borderWidth: Dp = 0.dp,
    borderColor: Color = Color.Transparent,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(shape)
                .background(MaterialTheme.colorScheme.outlineVariant)
                .then(
                    if (borderWidth > 0.dp) {
                        Modifier.border(borderWidth, borderColor, shape)
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (onClick != null) {
                        Modifier.clickable(onClick = onClick)
                    } else {
                        Modifier
                    },
                ),
        ) {
            if (!imageUrl.isNullOrEmpty()) {
                AppImage(
                    model = imageUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(size),
                )
            }
        }

        if (isOnline) {
            val badgeSize = size * 0.25f
            val badgeBorder = if (badgeSize > 8.dp) 2.dp else 1.5.dp
            Box(
                modifier = Modifier
                    .size(badgeSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
                    .border(badgeBorder, MaterialTheme.colorScheme.surface, CircleShape)
                    .align(Alignment.BottomEnd),
            )
        }
    }
}
