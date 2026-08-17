package com.smach.zapmancer.core.common.dto

import kotlinx.serialization.Serializable

@Serializable
enum class KycDocumentType {
    PASSPORT,
    DRIVERS_LICENSE,
    NATIONAL_ID
}

@Serializable
enum class KycStatus {
    PENDING,
    VERIFIED,
    FAILED,
    MANUAL_REVIEW
}

@Serializable
enum class KycLivenessPreset {
    EYE,
    HEAD_TURN,
    SMILE,
    MULTI_RANGE,
    FULL,
    PASSIVE_ONLY
}

@Serializable
data class KycInitRequest(
    val documentType: KycDocumentType,
    val livenessPreset: KycLivenessPreset = KycLivenessPreset.EYE
)

@Serializable
data class KycInitResponse(
    val verificationId: String,
    val livenessSessionId: String,
    val instruction: String,
    val preset: String,
    val expiresAtUtc: String
)

@Serializable
data class KycStatusResponse(
    val verificationId: String,
    val userId: String,
    val status: KycStatus,
    val documentType: KycDocumentType,
    val extractedName: String? = null,
    val extractedDob: String? = null,
    val extractedDocNumber: String? = null,
    val extractedExpiry: String? = null,
    val faceSimilarityScore: Double? = null,
    val livenessScore: Double? = null,
    val livenessPassed: Boolean = false,
    val isNameMatched: Boolean = false,
    val receiptSignature: String? = null,
    val receiptHash: String? = null,
    val reviewerNotes: String? = null,
    val createdAt: String,
    val updatedAt: String
)

@Serializable
data class KycReceiptResponse(
    val verificationId: String,
    val userId: String,
    val isValid: Boolean,
    val canonicalPayloadJson: String,
    val ed25519SignatureBase64: String,
    val receiptHashSha256: String,
    val publicKeyBase64: String,
    val verifiedAtUtc: String
)

@Serializable
data class KycAdminQueueItem(
    val verificationId: String,
    val userId: String,
    val username: String,
    val email: String,
    val documentType: KycDocumentType,
    val documentFrontUrl: String,
    val documentBackUrl: String? = null,
    val selfieUrl: String,
    val extractedName: String? = null,
    val faceSimilarityScore: Double? = null,
    val livenessScore: Double? = null,
    val livenessPassed: Boolean,
    val isNameMatched: Boolean,
    val status: KycStatus,
    val createdAt: String
)

@Serializable
data class KycAdminReviewRequest(
    val decision: KycStatus, // VERIFIED or FAILED
    val notes: String? = null
)
