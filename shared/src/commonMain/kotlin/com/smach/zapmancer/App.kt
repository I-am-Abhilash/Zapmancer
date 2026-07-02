package com.smach.zapmancer

// import androidx.compose.animation.AnimatedContent
// import androidx.compose.animation.fadeIn
// import androidx.compose.animation.fadeOut
// import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.smach.zapmancer.domain.repository.SettingsRepository
import com.smach.zapmancer.features.auth.screen.OnboardingScreen
import com.smach.zapmancer.features.common.theme.AppTheme
import com.smach.zapmancer.nav.AuthGraph
import com.smach.zapmancer.nav.MainGraph
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val settingsRepository: SettingsRepository = koinInject()
    val settingsState by settingsRepository.settingsFlow.collectAsState(initial = null)
    val isDarkMode = settingsState?.isDarkModeEnabled ?: isSystemInDarkTheme()
    val isClientMode = settingsState?.isClientModeEnabled ?: false

    val mainViewModel: MainViewModel = koinViewModel()
    val appState by mainViewModel.appState.collectAsState()

    AppTheme {
        when (val state = appState) {
            is AppState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is AppState.Onboarding -> {
                OnboardingScreen(
                    onFinished = { mainViewModel.completeOnboarding() },
                )
            }

            is AppState.Unauthenticated -> {
                AuthGraph(
                    onAuthSuccess = {},
                )
            }

            is AppState.Authenticated -> {
                MainGraph(
                    onLogout = { mainViewModel.logout() },
                    isClientMode = isClientMode,
                )
            }
        }
    }

//    AppTheme {
//        AnimatedContent(
//            targetState = appState,
//            transitionSpec = { fadeIn() togetherWith fadeOut() },
//        ) { state ->
//            when (state) {
//                is AppState.Loading -> {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .background(MaterialTheme.colorScheme.background),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
//                    }
//                }
//
//                is AppState.Onboarding -> {
//                    OnboardingScreen(
//                        onFinished = { mainViewModel.completeOnboarding() },
//                    )
//                }
//
//                is AppState.Unauthenticated -> {
//                    AuthGraph(
//                        onAuthSuccess = {},
//                    )
//                }
//
//                is AppState.Authenticated -> {
//                    MainGraph(
//                        onLogout = { mainViewModel.logout() },
//                        isClientMode = isClientMode,
//                    )
//                }
//            }
//        }
//    }
}
