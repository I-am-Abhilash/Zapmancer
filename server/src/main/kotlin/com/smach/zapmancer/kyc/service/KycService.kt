package com.smach.zapmancer.kyc.service

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitRequest
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsCapabilitiesResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsPassiveLivenessResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsWatchlistSearchResponse
import com.smach.zapmancer.core.common.dto.WatchlistDto
import com.smach.zapmancer.core.framework.storage.StorageService
import com.smach.zapmancer.kyc.client.OpenBiometricsClient
import com.smach.zapmancer.kyc.repository.KycRepository
import com.smach.zapmancer.kyc.security.Ed25519ReceiptService
import kotlin.time.Clock.System
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.UUID

@Serializable
data class CanonicalKycReceiptData(
    val verificationId: String,
    val userId: String,
    val documentType: String,
    val documentFrontSha256: String,
    val selfieSha256: String,
    val extractedName: String?,
    val faceSimilarityScore: Double?,
    val livenessScore: Double?,
    val decision: String,
    val timestampUtc: String
)

class KycService(
    private val kycRepository: KycRepository,
    private val openBiometricsClient: OpenBiometricsClient,
    private val receiptService: Ed25519ReceiptService,
    private val storageService: StorageService,
    private val bucketName: String = "zapmancer-assets"
) {
    /**
     * Initializes a KYC session and returns an active liveness gesture challenge.
     */
    suspend fun initializeKyc(userId: String, request: KycInitRequest): KycInitResponse {
        val verificationId = "kyc_" + UUID.randomUUID().toString().replace("-", "").take(16)
        val livenessInit = openBiometricsClient.createLivenessSession(request.livenessPreset.name.lowercase())

        kycRepository.createVerification(
            id = verificationId,
            userId = userId,
            documentType = request.documentType,
            documentFrontUrl = "",
            documentBackUrl = null,
            selfieUrl = "",
            status = KycStatus.PENDING
        )

        return KycInitResponse(
            verificationId = verificationId,
            livenessSessionId = livenessInit.session_id,
            instruction = livenessInit.instruction,
            preset = livenessInit.preset,
            expiresAtUtc = livenessInit.expires_at
        )
    }

    /**
     * Complete pipeline: Upload -> OCR -> Face Match -> Active Liveness -> Fraud Watchlist -> Decision -> Ed25519 Receipt -> DB
     */
    suspend fun processKycSubmission(
        userId: String,
        verificationId: String,
        livenessSessionId: String,
        documentType: KycDocumentType,
        frontBytes: ByteArray,
        backBytes: ByteArray?,
        selfieBytes: ByteArray,
        registeredUsername: String = ""
    ): KycStatusResponse {
        // 1. Upload files to Object Storage
        val frontPath = "kyc/$userId/$verificationId/front.jpg"
        val selfiePath = "kyc/$userId/$verificationId/selfie.jpg"
        val frontUrl = storageService.uploadFile(bucketName, frontPath, frontBytes, "image/jpeg")
        val selfieUrl = storageService.uploadFile(bucketName, selfiePath, selfieBytes, "image/jpeg")

        var backUrl: String? = null
        if (backBytes != null) {
            val backPath = "kyc/$userId/$verificationId/back.jpg"
            backUrl = storageService.uploadFile(bucketName, backPath, backBytes, "image/jpeg")
        }

        // 2. Perform Document OCR & Tamper Analysis
        val ocrResponse = openBiometricsClient.processDocument(frontBytes)
        val extractedName = ocrResponse.fields["name"]
        val extractedDob = ocrResponse.fields["dob"]
        val extractedDocNum = ocrResponse.fields["document_number"]
        val extractedExpiry = ocrResponse.fields["expiry_date"]

        // 3. Perform 1:1 Biometric Face Match (ID photo vs Live Selfie)
        val faceMatchResponse = openBiometricsClient.verifyFaces(frontBytes, selfieBytes)

        // 4. Verify Active Liveness & Anti-spoofing
        val livenessResponse = openBiometricsClient.evaluateLiveness(livenessSessionId, selfieBytes)

        // 5. Fraud Watchlist and Tamper Analysis (1:N Biometric Check against banned fraudsters)
        val watchlistCheck = openBiometricsClient.searchWatchlist(selfieBytes)
        val isBlacklisted = watchlistCheck.is_listed
        val isTampered = ocrResponse.confidence < 0.50 || ocrResponse.tampering_detected

        // 6. Name match evaluation
        val isNameMatched = if (!extractedName.isNullOrBlank() && registeredUsername.isNotBlank()) {
            val normalizedExtracted = extractedName.lowercase().replace(" ", "")
            val normalizedUser = registeredUsername.lowercase().replace(" ", "")
            normalizedExtracted.contains(normalizedUser) || normalizedUser.contains(normalizedExtracted)
        } else {
            true
        }

        // 7. Decision rule engine (auto-rejects blacklisted fraudsters immediately)
        val decision = if (isBlacklisted) {
            KycStatus.FAILED
        } else {
            evaluateDecision(
                similarity = faceMatchResponse.similarity,
                livenessPassed = livenessResponse.passed && livenessResponse.anti_spoof_passed && !isTampered,
                isNameMatched = isNameMatched
            )
        }

        // 8. Generate Ed25519 Cryptographic Receipt
        val nowUtc = System.now().toString()
        val frontSha256 = receiptService.sha256Hex(frontBytes)
        val selfieSha256 = receiptService.sha256Hex(selfieBytes)

        val receiptData = CanonicalKycReceiptData(
            verificationId = verificationId,
            userId = userId,
            documentType = documentType.name,
            documentFrontSha256 = frontSha256,
            selfieSha256 = selfieSha256,
            extractedName = extractedName,
            faceSimilarityScore = faceMatchResponse.similarity,
            livenessScore = livenessResponse.score,
            decision = decision.name,
            timestampUtc = nowUtc
        )
        val canonicalJson = Json.encodeToString(receiptData)
        val signature = receiptService.sign(canonicalJson)
        val receiptHash = receiptService.sha256Hex(canonicalJson.toByteArray(Charsets.UTF_8))

        // 9. Update record in database
        val result = kycRepository.updateVerificationResult(
            id = verificationId,
            status = decision,
            extractedName = extractedName,
            extractedDob = extractedDob,
            extractedDocNumber = extractedDocNum,
            extractedExpiry = extractedExpiry,
            faceSimilarityScore = faceMatchResponse.similarity,
            livenessScore = livenessResponse.score,
            livenessPassed = livenessResponse.passed,
            isNameMatched = isNameMatched,
            receiptSignature = signature,
            receiptHash = receiptHash
        )

        // 10. If verified, update project and user identity status
        if (decision == KycStatus.VERIFIED) {
            kycRepository.updateProjectIdentityFlags(userId, true)
        }

        return result ?: kycRepository.getVerificationById(verificationId)!!
    }

    suspend fun evaluatePassiveLiveness(imageBytes: ByteArray): OpenBiometricsPassiveLivenessResponse {
        return openBiometricsClient.evaluatePassiveLiveness(imageBytes)
    }

    suspend fun searchWatchlist(faceBytes: ByteArray): OpenBiometricsWatchlistSearchResponse {
        return openBiometricsClient.searchWatchlist(faceBytes)
    }

    suspend fun getWatchlists(): List<WatchlistDto> {
        return openBiometricsClient.getWatchlists()
    }

    suspend fun createWatchlist(name: String, description: String = ""): WatchlistDto? {
        return openBiometricsClient.createWatchlist(name, description)
    }

    suspend fun addFaceToWatchlist(watchlistId: String, name: String, faceBytes: ByteArray): Boolean {
        return openBiometricsClient.addFaceToWatchlist(watchlistId, name, faceBytes)
    }

    suspend fun getCapabilities(): OpenBiometricsCapabilitiesResponse {
        return openBiometricsClient.getCapabilities()
    }

    suspend fun getKycStatus(userId: String): KycStatusResponse? {
        return kycRepository.getLatestVerificationForUser(userId)
    }

    suspend fun getReceipt(verificationId: String): KycReceiptResponse? {
        val record = kycRepository.getVerificationById(verificationId) ?: return null
        val sig = record.receiptSignature ?: return null
        val rHash = record.receiptHash ?: return null

        val canonicalData = CanonicalKycReceiptData(
            verificationId = record.verificationId,
            userId = record.userId,
            documentType = record.documentType.name,
            documentFrontSha256 = rHash,
            selfieSha256 = rHash,
            extractedName = record.extractedName,
            faceSimilarityScore = record.faceSimilarityScore,
            livenessScore = record.livenessScore,
            decision = record.status.name,
            timestampUtc = record.createdAt
        )
        val canonicalJson = Json.encodeToString(canonicalData)
        val isValid = receiptService.verify(canonicalJson, sig, receiptService.publicKeyBase64)

        return KycReceiptResponse(
            verificationId = record.verificationId,
            userId = record.userId,
            isValid = isValid,
            canonicalPayloadJson = canonicalJson,
            ed25519SignatureBase64 = sig,
            receiptHashSha256 = rHash,
            publicKeyBase64 = receiptService.publicKeyBase64,
            verifiedAtUtc = record.updatedAt
        )
    }

    suspend fun getAdminQueue(): List<KycAdminQueueItem> {
        return kycRepository.getAdminReviewQueue()
    }

    suspend fun reviewKycSubmission(
        verificationId: String,
        adminUserId: String,
        decision: KycStatus,
        notes: String?
    ): KycStatusResponse? {
        val updated = kycRepository.updateAdminReview(verificationId, adminUserId, decision, notes)
        if (updated != null && decision == KycStatus.VERIFIED) {
            kycRepository.updateProjectIdentityFlags(updated.userId, true)
        }
        return updated
    }

    companion object {
        fun evaluateDecision(similarity: Double, livenessPassed: Boolean, isNameMatched: Boolean): KycStatus {
            if (!livenessPassed) return KycStatus.FAILED
            if (similarity < 0.60) return KycStatus.FAILED
            if (similarity in 0.60..0.80 || !isNameMatched) return KycStatus.MANUAL_REVIEW
            return KycStatus.VERIFIED
        }
    }
}
