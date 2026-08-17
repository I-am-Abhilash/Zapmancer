package com.smach.zapmancer.verification

import com.smach.zapmancer.auth.repository.AuthRepository
import com.smach.zapmancer.auth.repository.AuthUserRecord
import com.smach.zapmancer.auth.service.AuthService
import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.ErrorCode
import com.smach.zapmancer.core.verification.ResendEmailService
import com.smach.zapmancer.core.verification.TelnyxSmsService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AuthVerificationFlowTest {

    private val repository = mockk<AuthRepository>(relaxed = true)
    private val resendEmailService = mockk<ResendEmailService>(relaxed = true)
    private val telnyxSmsService = mockk<TelnyxSmsService>(relaxed = true)

    private val authService = AuthService(
        repository = repository,
        resendEmailService = resendEmailService,
        telnyxSmsService = telnyxSmsService
    )

    @Test
    fun testSendEmailVerificationDispatchesResendEmail() = runBlocking {
        val user = AuthUserRecord(
            id = "user_123",
            username = "alex_dev",
            email = "alex@zapmancer.com",
            passwordHash = "hash",
            isDeleted = false
        )
        coEvery { repository.findById("user_123") } returns user
        coEvery { repository.saveOtp(any(), any()) } returns Unit
        coEvery { resendEmailService.sendVerificationOtp(any(), any(), any()) } returns true

        val response = authService.sendEmailVerification("user_123")

        assertTrue(response.success)
        coVerify(exactly = 1) { repository.saveOtp("alex@zapmancer.com", any()) }
        coVerify(exactly = 1) { resendEmailService.sendVerificationOtp("alex@zapmancer.com", any(), "alex_dev") }
    }

    @Test
    fun testVerifyEmailSuccessSetsVerifiedFlag() = runBlocking {
        val user = AuthUserRecord(
            id = "user_123",
            username = "alex_dev",
            email = "alex@zapmancer.com",
            passwordHash = "hash",
            isDeleted = false
        )
        coEvery { repository.findById("user_123") } returns user
        coEvery { repository.verifyAndConsumeOtp("alex@zapmancer.com", "123456") } returns true
        coEvery { repository.setUserEmailVerified("user_123") } returns true

        val response = authService.verifyEmail("user_123", "123456")

        assertTrue(response.success)
        coVerify(exactly = 1) { repository.setUserEmailVerified("user_123") }
    }

    @Test
    fun testVerifyEmailInvalidCodeThrowsException() = runBlocking {
        val user = AuthUserRecord(
            id = "user_123",
            username = "alex_dev",
            email = "alex@zapmancer.com",
            passwordHash = "hash",
            isDeleted = false
        )
        coEvery { repository.findById("user_123") } returns user
        coEvery { repository.verifyAndConsumeOtp("alex@zapmancer.com", "999999") } returns false

        val ex = assertFailsWith<ApiException> {
            authService.verifyEmail("user_123", "999999")
        }
        assertEquals(ErrorCode.BAD_REQUEST, ex.code)
    }

    @Test
    fun testSendPhoneOtpDispatchesTelnyxSms() = runBlocking {
        coEvery { repository.savePhoneOtp(any(), any(), any()) } returns Unit
        coEvery { telnyxSmsService.sendOtpSms(any(), any()) } returns true

        val response = authService.sendPhoneOtp("user_123", "+14155552671")

        assertTrue(response.success)
        coVerify(exactly = 1) { repository.savePhoneOtp("user_123", "+14155552671", any()) }
        coVerify(exactly = 1) { telnyxSmsService.sendOtpSms("+14155552671", any()) }
    }

    @Test
    fun testSendPhoneOtpInvalidFormatThrowsException() = runBlocking {
        val ex = assertFailsWith<ApiException> {
            authService.sendPhoneOtp("user_123", "invalid-phone")
        }
        assertEquals(ErrorCode.BAD_REQUEST, ex.code)
    }

    @Test
    fun testVerifyPhoneOtpSuccessSetsVerifiedPhone() = runBlocking {
        coEvery { repository.verifyAndConsumePhoneOtp("user_123", "849201") } returns "+14155552671"
        coEvery { repository.setUserPhoneVerified("user_123", "+14155552671") } returns true

        val response = authService.verifyPhoneOtp("user_123", "849201")

        assertTrue(response.success)
        coVerify(exactly = 1) { repository.setUserPhoneVerified("user_123", "+14155552671") }
    }

    @Test
    fun testVerifyPhoneOtpInvalidCodeThrowsException() = runBlocking {
        coEvery { repository.verifyAndConsumePhoneOtp("user_123", "000000") } returns null

        val ex = assertFailsWith<ApiException> {
            authService.verifyPhoneOtp("user_123", "000000")
        }
        assertEquals(ErrorCode.BAD_REQUEST, ex.code)
    }
}
