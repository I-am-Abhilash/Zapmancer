//package com.smach.zapmancer
//
//import app.cash.turbine.test
//import com.smach.zapmancer.core.common.utils.DataError
//import com.smach.zapmancer.core.common.utils.Result
//import com.smach.zapmancer.core.network.session.SessionManager
//import com.smach.zapmancer.domain.model.User
//import com.smach.zapmancer.domain.repository.AuthRepository
//import dev.mokkery.answering.returns
//import dev.mokkery.every
//import dev.mokkery.everySuspend
//import dev.mokkery.matcher.any
//import dev.mokkery.mock
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.flowOf
//import kotlinx.coroutines.test.UnconfinedTestDispatcher
//import kotlinx.coroutines.test.runTest
//import kotlinx.coroutines.test.setMain
//import kotlin.test.AfterTest
//import kotlin.test.BeforeTest
//import kotlin.test.Test
//import kotlin.test.assertEquals
//
///**
// * Smoke test for [MainViewModel]:
// *  - When onboarding is not yet completed, appState resolves to [AppState.Onboarding].
// *  - When onboarding is completed but no token is stored, [AppState.Unauthenticated].
// *  - When onboarding is completed and a token is stored, [AppState.Authenticated].
// *
// * These cases catch regressions in the [MainViewModel]'s combine() logic and the
// * [AppState] derivation rules.
// */
//@OptIn(ExperimentalCoroutinesApi::class)
//class KoinBootTest {
//
//    private val dispatcher = UnconfinedTestDispatcher()
//
//    @BeforeTest
//    fun setUp() {
//        kotlinx.coroutines.Dispatchers.setMain(dispatcher)
//    }
//
//    @AfterTest
//    fun tearDown() {
//        kotlinx.coroutines.Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `appState is Onboarding when onboarding is not completed`() = runTest(dispatcher) {
//        val session = fakeSession(accessToken = null, onboardingCompleted = false)
//        val auth = fakeAuth(onboardingCompleted = false)
//        val vm = MainViewModel(auth, session)
//
//        vm.appState.test {
//            assertEquals(AppState.Onboarding, awaitItem())
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `appState is Unauthenticated when onboarding is done but no token`() = runTest(dispatcher) {
//        val session = fakeSession(accessToken = null, onboardingCompleted = true)
//        val auth = fakeAuth(onboardingCompleted = true)
//        val vm = MainViewModel(auth, session)
//
//        vm.appState.test {
//            assertEquals(AppState.Unauthenticated, awaitItem())
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `appState is Authenticated when onboarding is done and token is present`() = runTest(dispatcher) {
//        val session = fakeSession(accessToken = "tok-123", onboardingCompleted = true)
//        val auth = fakeAuth(onboardingCompleted = true)
//        val vm = MainViewModel(auth, session)
//
//        vm.appState.test {
//            assertEquals(AppState.Authenticated("tok-123"), awaitItem())
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    // ---- helpers ----
//
//    private fun fakeSession(
//        accessToken: String?,
//        onboardingCompleted: Boolean,
//    ): SessionManager {
//        val tokenFlow = MutableStateFlow(accessToken)
//        val onboardingFlow = MutableStateFlow(onboardingCompleted)
//        return mock<SessionManager> {
//            every { observeAccessToken() } returns tokenFlow
//            every { getOnboardingCompleted() } returns onboardingFlow
//            everySuspend { getAccessToken() } returns accessToken
//            everySuspend { getRefreshToken() } returns null
//        }
//    }
//
//    private fun fakeAuth(onboardingCompleted: Boolean): AuthRepository = mock<AuthRepository> {
//        every { isOnboardingCompleted() } returns flowOf(onboardingCompleted)
//        everySuspend { saveTokens(any(), any()) } returns Unit
//        everySuspend { setOnboardingCompleted(any()) } returns Result.com
//        everySuspend { logout() } returns Unit
//        everySuspend { login(any(), any()) } returns Result.Success(User(id = "1", email = "u@e.c"))
//        everySuspend { signUp(any(), any(), any()) } returns Result.Success(User(id = "1", email = "u@e.c"))
//        everySuspend { requestPasswordReset(any()) } returns Result.Success(Unit)
//        everySuspend { verifyOtp(any(), any()) } returns Result.Success(Unit)
//    }
//}
