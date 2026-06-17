Building a custom authentication flow in Compose Multiplatform (CMP) requires a clear separation
between your platform-agnostic UI/business logic and platform-specific authentication SDKs.Whether
you are using a custom backend (JWT tokens), OAuth (Google/Apple), or third-party identity
providers, this complete guide maps out a scalable architecture using standard Kotlin
Multiplatform (KMP) practices.🧱 1. Architecture OverviewTo keep your UI 100% shared, separate your
authentication system into three main layers:UI Layer (commonMain): Shared Compose text fields,
buttons, and validation.Domain/State Layer (commonMain): Shared ViewModels, state management, and
token refresh logic.Data Layer (commonMain & Platform Directories): Ktor HTTP client configuration,
secure storage wrappers, and expect/actual declarations for platform native logins.🔐 2. Custom
Token-Based Auth (JWT)If you manage login through your own backend API using a username/password or
email combination, you can write 100% shared Kotlin code without needing native platform hooks.Step
A: Shared Token StorageUse the Multiplatform Settings library to securely store your Access and
Refresh JWT tokens across Android (EncryptedSharedPreferences) and iOS (Keychain).kotlin//
commonMain/src/commonMain/kotlin/auth/AuthSettings.kt
import com.russhwolf.settings.Settings

class AuthRepository(private val settings: Settings) {
fun saveTokens(accessToken: String, refreshToken: String) {
settings.putString("KEY_ACCESS_TOKEN", accessToken)
settings.putString("KEY_REFRESH_TOKEN", refreshToken)
}

    fun getAccessToken(): String? = settings.getStringOrNull("KEY_ACCESS_TOKEN")
    
    fun clearAuth() {
        settings.remove("KEY_ACCESS_TOKEN")
        settings.remove("KEY_REFRESH_TOKEN")
    }

}
Use code with caution.Step B: Secure API Traffic with KtorConfigure Ktor Client with the Auth plugin
to automatically append bearer tokens to outgoing headers and handle token refresh side-effects
globally.kotlin// commonMain/src/commonMain/kotlin/network/NetworkClient.kt
import io.ktor.client.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*

fun createHttpClient(authRepository: AuthRepository) = HttpClient {
install(Auth) {
bearer {
loadTokens {
BearerTokens(
accessToken = authRepository.getAccessToken() ?: "",
refreshToken = authRepository.getRefreshToken() ?: ""
)
}
refreshTokens {
// Call your endpoint to get a fresh token using old refresh token
val newTokens = apiService.refreshTheTokens(authRepository.getRefreshToken())
authRepository.saveTokens(newTokens.access, newTokens.refresh)
BearerTokens(accessToken = newTokens.access, refreshToken = newTokens.refresh)
}
sendWithoutRequest { request ->
// Do not send tokens to public endpoints like login or register
request.url.pathSegments.contains("login")
}
}
}
}
Use code with caution.🌐 3. Third-Party Auth (Google, Apple, OAuth)For authentication requiring
native SDKs (e.g., Google Credential Manager or Apple Sign In), utilize Kotlin's expect/actual
design paradigm.Step A: Define the Contract in Common Codekotlin//
commonMain/src/commonMain/kotlin/auth/PlatformAuthenticator.kt
import androidx.compose.runtime.Composable

interface AuthResult {
data class Success(val idToken: String, val email: String?) : AuthResult
data class Failure(val error: String) : AuthResult
}

expect class PlatformAuthenticator {
suspend fun signInWithGoogle(): AuthResult
}
Use code with caution.Step B: Provide the Native iOS ImplementationOn the iOS side, use iosMain to
access the native Apple and Google frameworks directly through Kotlin/Native interop.kotlin//
iosMain/src/iosMain/kotlin/auth/PlatformAuthenticator.ios.kt
import platform.AuthenticationServices.*
import platform.UIKit.*
import kotlin.coroutines.resume

actual class PlatformAuthenticator(private val rootViewController: UIViewController) {
actual suspend fun signInWithGoogle(): AuthResult = suspendCancellableCoroutine { continuation ->
// Direct execution using native iOS frameworks (e.g. GoogleSignIn SDK via CocoaPods/SPM)
// Returns AuthResult.Success(idToken, email) upon success
}
}
Use code with caution.Step C: Provide the Native Android ImplementationIn androidMain, interact with
Android's CredentialManager API.kotlin//
androidMain/src/androidMain/kotlin/auth/PlatformAuthenticator.android.kt
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest

actual class PlatformAuthenticator(private val context: Context) {
actual suspend fun signInWithGoogle(): AuthResult {
val credentialManager = CredentialManager.create(context)
// Request and process Google Sign-In intent here
return try {
val result = credentialManager.getCredential(context, getCredentialRequest)
AuthResult.Success(idToken = extractToken(result), email = null)
} catch (e: Exception) {
AuthResult.Failure(e.localizedMessage ?: "Unknown Error")
}
}
}
Use code with caution.🎨 4. Managing Global Auth State in Compose UITrack your session state in your
shared presentation layer to cleanly toggle between your onboarding flow and main authenticated
application flow.kotlin// commonMain/src/commonMain/kotlin/auth/AuthViewModel.kt
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

sealed interface SessionState {
object Loading : SessionState
object Unauthenticated : SessionState
data class Authenticated(val token: String) : SessionState
}

class MainViewModel(private val repository: AuthRepository) {
private val _session = MutableStateFlow<SessionState>(SessionState.Loading)
val session: StateFlow<SessionState> = _session

    fun checkStatus() {
        val token = repository.getAccessToken()
        if (token != null) {
            _session.value = SessionState.Authenticated(token)
        } else {
            _session.value = SessionState.Unauthenticated
        }
    }

}
Use code with caution.Tie everything neatly into your entry composable app view:kotlin//
commonMain/src/commonMain/kotlin/App.kt
import androidx.compose.runtime.*

@Composable
fun App(viewModel: MainViewModel) {
val sessionState by viewModel.session.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.checkStatus()
    }

    when (val state = sessionState) {
        is SessionState.Loading -> SplashScreen()
        is SessionState.Unauthenticated -> LoginScreen(onLoginSuccess = { viewModel.checkStatus() })
        is SessionState.Authenticated -> MainDashboardScreen(token = state.token)
    }

}
Use code with caution.⚡ Shortcuts & Library AlternativesIf you prefer not to write boilerplate code
for platform-specific providers, consider utilizing these production-ready KMP open-source options:
KMP Auth (by GitLive): The premier choice if you prefer managing auth over Firebase Auth with shared
Kotlin APIs across iOS and Android.Compose Multiplatform Auth Library: Offers pre-packaged UI
buttons and integrated configurations for signing in through Google, Apple, and Microsoft within
target environments.