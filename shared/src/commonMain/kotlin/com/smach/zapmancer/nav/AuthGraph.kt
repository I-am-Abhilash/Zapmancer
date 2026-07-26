package com.smach.zapmancer.nav

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.smach.zapmancer.presentation.auth.screen.ForgotPasswordScreen
import com.smach.zapmancer.presentation.auth.screen.LoginScreen
import com.smach.zapmancer.presentation.auth.screen.SignupScreen
import com.smach.zapmancer.presentation.auth.screen.VerificationScreen

/**
 * AuthGraph handles the unauthenticated navigation stack:
 * Login → Signup, Forgot Password, Verification.
 */
@Composable
fun AuthGraph(
    onAuthSuccess: () -> Unit,
) {
    val state = rememberNavigationState(Screen.Login, setOf(Screen.Login))
    val navigator = MainNavigator(state)

    NavDisplay(
        entries = state.toEntries(authEntryProvider(navigator, onAuthSuccess)),
        onBack = { navigator.goBack() },
    )
}

@Composable
private fun authEntryProvider(
    navigator: MainNavigator,
    onAuthSuccess: () -> Unit,
): (NavKey) -> NavEntry<NavKey> = entryProvider {
    entry<Screen.Login> {
        LoginScreen(
            onNavigateToForgot = { navigator.navigate(Screen.ForgotPassword) },
            onNavigateToSignup = { navigator.navigate(Screen.Signup) },
            onLoginSuccess = onAuthSuccess,
        )
    }
    entry<Screen.Signup> {
        SignupScreen(
            onNavigateToLogin = { navigator.navigate(Screen.Login) },
            onSignupSuccess = { email ->
                navigator.navigate(Screen.Verification(email = email))
            },
        )
    }
    entry<Screen.ForgotPassword> {
        ForgotPasswordScreen(
            onBackToLogin = { navigator.navigate(Screen.Login) },
        )
    }
    entry<Screen.Verification> { screen ->
        VerificationScreen(
            onBack = { navigator.goBack() },
            email = screen.email,
            onVerificationSuccess = onAuthSuccess,
        )
    }
}
