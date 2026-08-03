// package com.smach.zapmancer.presentation.common.adaptive
//
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.fillMaxWidth
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.widthIn
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Alignment
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
//
// @Composable
// fun ResponsiveContainer(
//    windowLayout: WindowLayout,
//    modifier: Modifier = Modifier,
//    contentAlignment: Alignment = Alignment.TopStart,
//    content: @Composable () -> Unit,
// ) {
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .padding(horizontal = windowLayout.screenHorizontalPaddingDp.dp),
//        contentAlignment = contentAlignment,
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .widthIn(max = windowLayout.contentMaxWidthDp.dp)
//                .align(contentAlignment),
//        ) {
//            content()
//        }
//    }
// }
