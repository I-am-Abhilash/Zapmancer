package com.smach.zapmancer.kyc

import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.core.common.dto.OpenBiometricsPassiveLivenessResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsWatchlistSearchResponse
import com.smach.zapmancer.core.common.dto.WatchlistSearchMatch
import com.smach.zapmancer.core.framework.storage.StorageService
import com.smach.zapmancer.kyc.client.OpenBiometricsClient
import com.smach.zapmancer.kyc.client.OpenBiometricsDocumentResponse
import com.smach.zapmancer.kyc.client.OpenBiometricsLivenessEvaluateResponse
import com.smach.zapmancer.kyc.client.OpenBiometricsVerifyResponse
import com.smach.zapmancer.kyc.repository.KycRepository
import com.smach.zapmancer.kyc.security.Ed25519ReceiptService
import com.smach.zapmancer.kyc.service.KycService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OpenBiometricsFullPowerTest {

    private val kycRepository = mockk<KycRepository>(relaxed = true)
    private val openBiometricsClient = mockk<OpenBiometricsClient>(relaxed = true)
    private val receiptService = Ed25519ReceiptService()
    private val storageService = mockk<StorageService>(relaxed = true)

    private val kycService = KycService(
        kycRepository = kycRepository,
        openBiometricsClient = openBiometricsClient,
        receiptService = receiptService,
        storageService = storageService,
    )

    init {
        coEvery {
            kycRepository.updateVerificationResult(
                id = any(),
                status = any(),
                extractedName = any(),
                extractedDob = any(),
                extractedDocNumber = any(),
                extractedExpiry = any(),
                faceSimilarityScore = any(),
                livenessScore = any(),
                livenessPassed = any(),
                isNameMatched = any(),
                receiptSignature = any(),
                receiptHash = any(),
            )
        } answers {
            val statusArg = secondArg<KycStatus>()
            com.smach.zapmancer.core.common.dto.KycStatusResponse(
                verificationId = firstArg(),
                userId = "test_user",
                status = statusArg,
                documentType = KycDocumentType.PASSPORT,
                createdAt = "2026-08-18T10:00:00Z",
                updatedAt = "2026-08-18T10:00:00Z",
            )
        }
    }

    @Test
    fun testPassiveLivenessEvaluationReturnsScoreAndAntiSpoof() {
        runBlocking {
            coEvery { openBiometricsClient.evaluatePassiveLiveness(any()) } returns OpenBiometricsPassiveLivenessResponse(
                passed = true,
                score = 0.96,
                anti_spoof_passed = true,
                screen_glare_detected = false,
                moire_pattern_detected = false,
                deepfake_probability = 0.01,
            )

            val result = kycService.evaluatePassiveLiveness(ByteArray(10))

            assertTrue(result.passed)
            assertTrue(result.anti_spoof_passed)
            assertEquals(0.96, result.score)
            assertFalse(result.screen_glare_detected)
        }
    }

    @Test
    fun testWatchlistBlacklistBlocksFraudSubmission() {
        runBlocking {
            // Mock document OCR
            coEvery { openBiometricsClient.processDocument(any()) } returns OpenBiometricsDocumentResponse(
                document_type = "PASSPORT",
                confidence = 0.95,
                tampering_detected = false,
                fields = mapOf("name" to "Known Scammer"),
            )
            // Mock 1:1 match
            coEvery { openBiometricsClient.verifyFaces(any(), any()) } returns OpenBiometricsVerifyResponse(
                is_match = true,
                similarity = 0.95,
                distance = 0.05,
            )
            // Mock liveness pass
            coEvery { openBiometricsClient.evaluateLiveness(any(), any()) } returns OpenBiometricsLivenessEvaluateResponse(
                passed = true,
                score = 0.95,
                anti_spoof_passed = true,
            )
            // Mock watchlist hit (Banned fraudster found)
            coEvery { openBiometricsClient.searchWatchlist(any(), any()) } returns OpenBiometricsWatchlistSearchResponse(
                is_listed = true,
                highest_similarity = 0.94,
                matches = listOf(
                    WatchlistSearchMatch(
                        face_id = "face_999",
                        name = "Banned Fraudster",
                        similarity = 0.94,
                        watchlist_id = "fraud_blacklist",
                        matched_at = "2026-08-18T10:00:00Z",
                    ),
                ),
            )

            val result = kycService.processKycSubmission(
                userId = "user_scammer",
                verificationId = "kyc_test_001",
                livenessSessionId = "sess_1",
                documentType = KycDocumentType.PASSPORT,
                frontBytes = ByteArray(10),
                backBytes = null,
                selfieBytes = ByteArray(10),
                registeredUsername = "Known Scammer",
            )

            // Verifies the KYC decision was auto-rejected because of fraud blacklist
            assertEquals(KycStatus.FAILED, result.status)
        }
    }

    @Test
    fun testCleanSubmissionWithHighBiometricsApproves() {
        runBlocking {
            // Clean watchlist search (Not on any blacklist)
            coEvery { openBiometricsClient.searchWatchlist(any(), any()) } returns OpenBiometricsWatchlistSearchResponse(
                is_listed = false,
                highest_similarity = 0.12,
                matches = emptyList(),
            )
            coEvery { openBiometricsClient.processDocument(any()) } returns OpenBiometricsDocumentResponse(
                document_type = "PASSPORT",
                confidence = 0.96,
                tampering_detected = false,
                fields = mapOf("name" to "Alice Verified"),
            )
            coEvery { openBiometricsClient.verifyFaces(any(), any()) } returns OpenBiometricsVerifyResponse(
                is_match = true,
                similarity = 0.92,
                distance = 0.08,
            )
            coEvery { openBiometricsClient.evaluateLiveness(any(), any()) } returns OpenBiometricsLivenessEvaluateResponse(
                passed = true,
                score = 0.95,
                anti_spoof_passed = true,
            )

            val result = kycService.processKycSubmission(
                userId = "user_clean",
                verificationId = "kyc_test_002",
                livenessSessionId = "sess_2",
                documentType = KycDocumentType.PASSPORT,
                frontBytes = ByteArray(10),
                backBytes = null,
                selfieBytes = ByteArray(10),
                registeredUsername = "Alice Verified",
            )

            assertEquals(KycStatus.VERIFIED, result.status)
        }
    }
}
