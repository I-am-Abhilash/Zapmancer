package com.smach.zapmancer.kyc

import com.smach.zapmancer.kyc.security.Ed25519ReceiptService
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class Ed25519ReceiptServiceTest {

    @Test
    fun testGenerateAndSignValidReceipt() {
        val service = Ed25519ReceiptService()
        val publicKey = service.publicKeyBase64
        assertNotNull(publicKey)
        assertTrue(publicKey.isNotBlank())

        val payload = """{"decision":"VERIFIED","userId":"user-123","similarity":0.92,"timestampUtc":"2026-08-17T15:00:00Z"}"""
        val signature = service.sign(payload)
        assertNotNull(signature)
        assertTrue(signature.isNotBlank())

        // Verify valid signature
        val isValid = service.verify(payload, signature, publicKey)
        assertTrue(isValid, "Valid signature must verify successfully")
    }

    @Test
    fun testTamperedPayloadFailsVerification() {
        val service = Ed25519ReceiptService()
        val publicKey = service.publicKeyBase64

        val originalPayload = """{"decision":"VERIFIED","userId":"user-123","similarity":0.92}"""
        val signature = service.sign(originalPayload)

        // Mutate payload by 1 character
        val tamperedPayload = """{"decision":"VERIFIED","userId":"user-999","similarity":0.92}"""
        val isValid = service.verify(tamperedPayload, signature, publicKey)
        assertFalse(isValid, "Tampered payload must fail cryptographic verification")
    }

    @Test
    fun testSha256Hex() {
        val service = Ed25519ReceiptService()
        val text = "test-document-bytes"
        val hash1 = service.sha256Hex(text.toByteArray(Charsets.UTF_8))
        val hash2 = service.sha256Hex(text.toByteArray(Charsets.UTF_8))
        assertEquals(hash1, hash2)
        assertEquals(64, hash1.length, "SHA-256 hex must be 64 characters long")
    }
}
