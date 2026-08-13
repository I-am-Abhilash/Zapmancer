package com.smach.zapmancer.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun VerticalDivider(
    color: Color = MaterialTheme.colorScheme.outlineVariant,
    height: Dp = 40.dp,
) {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(height)
            .background(color),
    )
}
