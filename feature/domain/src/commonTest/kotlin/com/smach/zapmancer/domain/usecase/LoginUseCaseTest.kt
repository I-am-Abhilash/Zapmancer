package com.smach.zapmancer.domain.usecase

import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result
import com.smach.zapmancer.domain.model.User
import com.smach.zapmancer.domain.repository.AuthRepository
import dev.mokkery.answering.returns
import dev.mokkery.answering.throws
import dev.mokkery.everySuspend
import dev.mokkery.mock
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests [LoginUseCase]:
 *  - On repo Success, use-case returns the same Result.Success
 *  - On repo Error, use-case returns the same Result.Error (no swallowing).
 */
class LoginUseCaseTest {

    @Test
    fun `invoke returns Success when repository succeeds`() = runTest {
        val user = User(id = "1", email = "user@example.com", accessToken = "abc", refreshToken = "xyz")
        val repository = mock<AuthRepository> {
            everySuspend { login("user@example.com", "pw") } returns Result.Success(user)
        }
        val useCase = LoginUseCase(repository)

        val result = useCase("user@example.com", "pw")

        assertTrue(result is Result.Success)
        assertEquals(user, result.data)
    }

    @Test
    fun `invoke returns Error when repository returns Error`() = runTest {
        val repository = mock<AuthRepository> {
            everySuspend { login("user@example.com", "pw") } returns Result.Error(DataError.Network.UNAUTHORIZED)
        }
        val useCase = LoginUseCase(repository)

        val result = useCase("user@example.com", "pw")

        assertTrue(result is Result.Error)
        assertEquals(DataError.Network.UNAUTHORIZED, result.error)
    }

    @Test
    fun `invoke does not catch repository exceptions`() = runTest {
        val boom = IllegalStateException("network down")
        val repository = mock<AuthRepository> {
            everySuspend { login("user@example.com", "pw") } throws boom
        }
        val useCase = LoginUseCase(repository)

        val thrown = runCatching { useCase("user@example.com", "pw") }.exceptionOrNull()
        assertEquals(boom, thrown, "Use-case must not swallow repository exceptions — callers depend on it.")
    }
}
