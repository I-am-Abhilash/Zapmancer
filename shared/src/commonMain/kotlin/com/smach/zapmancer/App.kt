package com.smach.zapmancer

import androidx.compose.runtime.Composable
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.nav.MainGraph

@Composable
fun App() {
//    val viewModel: MainViewModel = koinInject()
//    val sessionState by viewModel.session.collectAsState()

//    AppTheme {
//        when (sessionState) {
//            SessionState.Loading -> {
////                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
////                    Text("Loading...", style = MaterialTheme.typography.headlineMedium)
////                }
//            }
//
//            SessionState.Unauthenticated -> {
//                AuthGraph(
//                    onAuthSuccess = { /* MainViewModel will automatically update via Flow */ },
//                )
//            }
//
//            is SessionState.Authenticated -> {
//                MainGraph()
//            }
//        }
//    }

    AppTheme {
        MainGraph()
    }
}
