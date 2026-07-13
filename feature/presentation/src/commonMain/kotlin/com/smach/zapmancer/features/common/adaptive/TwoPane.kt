// package com.smach.zapmancer.features.common.adaptive
//
// import androidx.compose.foundation.layout.Box
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.fillMaxHeight
// import androidx.compose.foundation.layout.fillMaxSize
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.width
// import androidx.compose.runtime.Composable
// import androidx.compose.ui.Modifier
// import androidx.compose.ui.unit.dp
//
// @Composable
// fun TwoPane(
//    windowLayout: WindowLayout,
//    primary: @Composable () -> Unit,
//    secondary: @Composable () -> Unit,
//    modifier: Modifier = Modifier,
// ) {
//    if (windowLayout.isCompact) {
//        Box(modifier = modifier.fillMaxSize()) { primary() }
//        return
//    }
//
//    val primaryFraction = when (windowLayout) {
//        WindowLayout.Compact -> 1f
//        WindowLayout.Medium -> 0.42f
//        WindowLayout.Expanded -> 0.34f
//    }
//
//    Row(modifier = modifier.fillMaxSize()) {
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .width(0.dp)
//                .weight(primaryFraction)
//                .padding(end = 8.dp),
//        ) { primary() }
//        Box(
//            modifier = Modifier
//                .fillMaxHeight()
//                .weight(1f - primaryFraction)
//                .padding(start = 8.dp),
//        ) { secondary() }
//    }
// }
