package com.smach.zapmancer.verification

import com.smach.zapmancer.core.common.dto.SendPhoneOtpRequest
import com.smach.zapmancer.core.common.dto.VerificationStatusResponse
import com.smach.zapmancer.core.common.dto.VerifyEmailRequest
import com.smach.zapmancer.core.common.dto.VerifyPhoneOtpRequest
import com.smach.zapmancer.core.verification.ResendEmailService
import com.smach.zapmancer.core.verification.TelnyxSmsService
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class VerificationPayloadTest {

    @Test
    fun testVerificationDtosSerialization() {
        val json = Json { ignoreUnknownKeys = true }

        // Test Phone OTP request
        val phoneReq = SendPhoneOtpRequest(phoneNumber = "+14155552671")
        val phoneJson = json.encodeToString(phoneReq)
        assertTrue(phoneJson.contains("+14155552671"))

        val decodedPhoneReq = json.decodeFromString<SendPhoneOtpRequest>(phoneJson)
        assertEquals("+14155552671", decodedPhoneReq.phoneNumber)

        // Test Phone Verify request
        val verifyReq = VerifyPhoneOtpRequest(code = "839201")
        val verifyJson = json.encodeToString(verifyReq)
        val decodedVerify = json.decodeFromString<VerifyPhoneOtpRequest>(verifyJson)
        assertEquals("839201", decodedVerify.code)

        // Test Email Verify request
        val emailReq = VerifyEmailRequest(code = "123456")
        val emailJson = json.encodeToString(emailReq)
        val decodedEmail = json.decodeFromString<VerifyEmailRequest>(emailJson)
        assertEquals("123456", decodedEmail.code)

        // Test VerificationStatusResponse
        val status = VerificationStatusResponse(
            isEmailVerified = true,
            isPhoneVerified = true,
            isIdentityVerified = false,
            email = "alex@zapmancer.com",
            phoneNumber = "+14155552671",
        )
        val statusJson = json.encodeToString(status)
        assertTrue(statusJson.contains("alex@zapmancer.com"))
        val decodedStatus = json.decodeFromString<VerificationStatusResponse>(statusJson)
        assertTrue(decodedStatus.isEmailVerified)
        assertTrue(decodedStatus.isPhoneVerified)
    }

    @Test
    fun testResendAndTelnyxMockFallback() = runBlocking {
        // Without API keys, services should return true (mock logging mode) and not throw exceptions
        val resendMock = ResendEmailService(apiKey = null)
        val emailSuccess = resendMock.sendVerificationOtp("test@zapmancer.com", "654321", "TestUser")
        assertTrue(emailSuccess)

        val telnyxMock = TelnyxSmsService(apiKey = null)
        val smsSuccess = telnyxMock.sendOtpSms("+14155552671", "654321")
        assertTrue(smsSuccess)
    }
}
