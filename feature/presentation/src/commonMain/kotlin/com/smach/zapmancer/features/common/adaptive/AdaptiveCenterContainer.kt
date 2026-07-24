package com.smach.zapmancer.features.common.adaptive

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AdaptiveCenterContainer(
    modifier: Modifier = Modifier,
    maxWidth: Dp = 400.dp,
    alignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = alignment
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = maxWidth)
        ) {
            content()
        }
    }
}