package com.smach.zapmancer.presentation.common.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveCenterContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp? = null,
    alignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val effectiveMaxWidth = maxWidth ?: when {
        windowSizeClass.isCompactWidth -> 440.dp
        windowSizeClass.isMediumWidth -> 600.dp
        windowSizeClass.isExpandedWidth -> 840.dp
        else -> 440.dp
    }

    val horizontalPadding = when {
        windowSizeClass.isCompactWidth -> 16.dp
        windowSizeClass.isMediumWidth -> 32.dp
        windowSizeClass.isExpandedWidth -> 48.dp
        else -> 16.dp
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalPadding, vertical = 24.dp),
            contentAlignment = alignment,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = effectiveMaxWidth),
            ) {
                content()
            }
        }
    }
}
