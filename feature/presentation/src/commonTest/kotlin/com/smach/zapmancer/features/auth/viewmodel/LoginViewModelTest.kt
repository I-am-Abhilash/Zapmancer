// package com.smach.zapmancer.presentation.auth.viewmodel
//
// import app.cash.turbine.test
// import com.smach.zapmancer.core.common.utils.DataError
// import com.smach.zapmancer.core.common.utils.Result
// import com.smach.zapmancer.domain.model.User
// import com.smach.zapmancer.domain.usecase.LoginUseCase
// import dev.mokkery.answering.returns
// import dev.mokkery.everySuspend
// import dev.mokkery.mock
// import kotlinx.coroutines.Dispatchers
// import kotlinx.coroutines.ExperimentalCoroutinesApi
// import kotlinx.coroutines.test.StandardTestDispatcher
// import kotlinx.coroutines.test.resetMain
// import kotlinx.coroutines.test.runTest
// import kotlinx.coroutines.test.setMain
// import kotlin.test.AfterTest
// import kotlin.test.BeforeTest
// import kotlin.test.Test
// import kotlin.test.assertEquals
// import kotlin.test.assertNull
//
// /**
// * Verifies [LoginViewModel]:
// *  - Loading → Success on repo Success
// *  - Loading → Error with toUserMessage() text on repo Error
// */
// @OptIn(ExperimentalCoroutinesApi::class)
// class LoginViewModelTest {
//
//    private val dispatcher = StandardTestDispatcher()
//
//    @BeforeTest
//    fun setUp() {
//        Dispatchers.setMain(dispatcher)
//    }
//
//    @AfterTest
//    fun tearDown() {
//        Dispatchers.resetMain()
//    }
//
//    @Test
//    fun `submit transitions to isSuccess when use-case returns Success`() = runTest(dispatcher) {
//        val user = User(id = "1", email = "a@b.c", accessToken = "x", refreshToken = "y")
//        val useCase = mock<LoginUseCase> {
//            everySuspend { invoke("a@b.c", "pw") } returns Result.Success(user)
//        }
//        val vm = LoginViewModel(useCase)
//
//        vm.uiState.test {
//            assertEquals(false, awaitItem().isLoading, "Initial state is not loading")
//
//            vm.onEvent(LoginEvent.Submit)
//
//            // First state update: isLoading=true
//            assertEquals(true, awaitItem().isLoading)
//            // Final state: isSuccess=true, isLoading=false, error=null
//            val final = awaitItem()
//            assertEquals(false, final.isLoading)
//            assertEquals(true, final.isSuccess)
//            assertNull(final.error)
//            cancelAndConsumeRemainingEvents()
//        }
//    }
//
//    @Test
//    fun `submit sets error message when use-case returns Error`() = runTest(dispatcher) {
//        val useCase = mock<LoginUseCase> {
//            everySuspend {
//                invoke(
//                    "a@b.c",
//                    "pw",
//                )
//            } returns Result.Error(DataError.Network.UNAUTHORIZED)
//        }
//        val vm = LoginViewModel(useCase)
//
//        vm.uiState.test {
//            assertEquals(false, awaitItem().isLoading)
//
//            vm.onEvent(LoginEvent.Submit)
//
//            // First: loading
//            assertEquals(true, awaitItem().isLoading)
//            // Final: error message set, isSuccess stays false
//            val final = awaitItem()
//            assertEquals(false, final.isLoading)
//            assertEquals(false, final.isSuccess)
//            assertEquals(
//                "Session expired or invalid credentials. Please log in again.",
//                final.error,
//            )
//            cancelAndConsumeRemainingEvents()
//        }
//    }
// }
