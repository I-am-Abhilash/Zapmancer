package com.smach.zapmancer.kyc.service

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitRequest
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.kyc.client.OpenBiometricsClient
import com.smach.zapmancer.kyc.repository.KycRepository
import com.smach.zapmancer.kyc.security.Ed25519ReceiptService
import com.smach.zapmancer.core.framework.storage.StorageService
import kotlinx.datetime.Clock
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
     * Start a new KYC session with an active/passive liveness challenge.
     */
    suspend fun initializeKyc(userId: String, request: KycInitRequest): KycInitResponse {
        val livenessSession = openBiometricsClient.createLivenessSession(request.livenessPreset.name.lowercase())
        val verificationId = UUID.randomUUID().toString()

        return KycInitResponse(
            verificationId = verificationId,
            livenessSessionId = livenessSession.session_id,
            instruction = livenessSession.instruction,
            preset = livenessSession.preset,
            expiresAtUtc = livenessSession.expires_at
        )
    }

    /**
     * Submit uploaded document and selfie frames for AI verification and Ed25519 signing.
     */
    suspend fun processKycSubmission(
        userId: String,
        verificationId: String,
        livenessSessionId: String,
        documentType: KycDocumentType,
        frontBytes: ByteArray,
        backBytes: ByteArray?,
        selfieBytes: ByteArray,
        registeredUsername: String
    ): KycStatusResponse {
        // 1. Upload files to storage
        val frontUrl = storageService.uploadFile(bucketName, "kyc/$userId/${verificationId}_front.jpg", frontBytes, "image/jpeg")
        val backUrl = backBytes?.let {
            storageService.uploadFile(bucketName, "kyc/$userId/${verificationId}_back.jpg", it, "image/jpeg")
        }
        val selfieUrl = storageService.uploadFile(bucketName, "kyc/$userId/${verificationId}_selfie.jpg", selfieBytes, "image/jpeg")


        // 2. Initial record creation
        kycRepository.createVerification(
            id = verificationId,
            userId = userId,
            documentType = documentType,
            documentFrontUrl = frontUrl,
            documentBackUrl = backUrl,
            selfieUrl = selfieUrl,
            status = KycStatus.PENDING
        )

        // 3. Document OCR / MRZ processing
        val docResponse = openBiometricsClient.processDocument(frontBytes)
        val extractedName = docResponse.fields["name"] ?: docResponse.mrz["name"]
        val extractedDob = docResponse.fields["dob"] ?: docResponse.mrz["dob"]
        val extractedDocNum = docResponse.fields["document_number"] ?: docResponse.mrz["document_number"]
        val extractedExpiry = docResponse.fields["expiry"] ?: docResponse.mrz["expiry"]

        // 4. Liveness evaluation
        val livenessResponse = openBiometricsClient.evaluateLiveness(livenessSessionId, selfieBytes)

        // 5. 1:1 Face Match verification
        val faceMatchResponse = openBiometricsClient.verifyFaces(frontBytes, selfieBytes)

        // 6. Name match evaluation
        val isNameMatched = if (extractedName != null && registeredUsername.isNotBlank()) {
            val normalizedExtracted = extractedName.lowercase().replace(" ", "")
            val normalizedUser = registeredUsername.lowercase().replace(" ", "")
            normalizedExtracted.contains(normalizedUser) || normalizedUser.contains(normalizedExtracted)
        } else {
            true // Default to true if name not found in OCR to allow manual review if needed
        }

        // 7. Decision rule engine
        val decision = evaluateDecision(
            similarity = faceMatchResponse.similarity,
            livenessPassed = livenessResponse.passed && livenessResponse.anti_spoof_passed,
            isNameMatched = isNameMatched
        )

        // 8. Generate Ed25519 Cryptographic Receipt
        val nowUtc = Clock.System.now().toString()
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

        return result ?: kycRepository.getVerificationById(verificationId)!
    }

    suspend fun getKycStatus(userId: String): KycStatusResponse? {
        return kycRepository.getLatestVerificationForUser(userId)
    }

    suspend fun getReceipt(verificationId: String): KycReceiptResponse? {
        val record = kycRepository.getVerificationById(verificationId) ?: return null
        if (record.receiptSignature == null || record.receiptHash == null) return null

        val canonicalData = CanonicalKycReceiptData(
            verificationId = record.verificationId,
            userId = record.userId,
            documentType = record.documentType.name,
            documentFrontSha256 = record.receiptHash, // reference hash
            selfieSha256 = record.receiptHash,
            extractedName = record.extractedName,
            faceSimilarityScore = record.faceSimilarityScore,
            livenessScore = record.livenessScore,
            decision = record.status.name,
            timestampUtc = record.createdAt
        )
        val canonicalJson = Json.encodeToString(canonicalData)
        val isValid = receiptService.verify(canonicalJson, record.receiptSignature, receiptService.publicKeyBase64)

        return KycReceiptResponse(
            verificationId = record.verificationId,
            userId = record.userId,
            isValid = isValid,
            canonicalPayloadJson = canonicalJson,
            ed25519SignatureBase64 = record.receiptSignature,
            receiptHashSha256 = record.receiptHash,
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
        if (decision == KycStatus.VERIFIED && updated != null) {
            kycRepository.updateProjectIdentityFlags(updated.userId, true)
        }
        return updated
    }

    companion object {
        fun evaluateDecision(similarity: Double, livenessPassed: Boolean, isNameMatched: Boolean): KycStatus {
            if (!livenessPassed) return KycStatus.FAILED
            if (similarity >= 0.85 && isNameMatched) return KycStatus.VERIFIED
            if (similarity >= 0.65) return KycStatus.MANUAL_REVIEW
            return KycStatus.FAILED
        }
    }
}
