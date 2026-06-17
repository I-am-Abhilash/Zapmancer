package com.smach.zapmancer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.nav.AuthGraph
import com.smach.zapmancer.nav.MainGraph
import org.koin.compose.koinInject

@Composable
fun App() {
    val viewModel: MainViewModel = koinInject()
    val sessionState by viewModel.session.collectAsState()

    AppTheme {
        when (sessionState) {
            SessionState.Loading -> {
//                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//                    Text("Loading...", style = MaterialTheme.typography.headlineMedium)
//                }
            }

            SessionState.Unauthenticated -> {
                AuthGraph(
                    onAuthSuccess = { /* MainViewModel will automatically update via Flow */ },
                )
            }

            is SessionState.Authenticated -> {
                MainGraph()
            }
        }
    }
}
