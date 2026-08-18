package com.smach.zapmancer.domain.repository

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycAdminReviewRequest
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitRequest
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycLivenessPreset
import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.dto.KycStatus
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsCapabilitiesResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsPassiveLivenessResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsWatchlistSearchResponse
import com.smach.zapmancer.core.common.utils.DataError
import com.smach.zapmancer.core.common.utils.Result

interface KycRepository {
    suspend fun getReceipt(verificationId: String): Result<KycReceiptResponse, DataError.Network>

    suspend fun getCapabilities(): Result<OpenBiometricsCapabilitiesResponse, DataError.Network>

    suspend fun initializeKyc(
        documentType: KycDocumentType,
        livenessPreset: KycLivenessPreset = KycLivenessPreset.EYE,
    ): Result<KycInitResponse, DataError.Network>

    suspend fun submitKyc(
        verificationId: String,
        livenessSessionId: String,
        documentType: KycDocumentType,
        frontBytes: ByteArray,
        backBytes: ByteArray? = null,
        selfieBytes: ByteArray,
    ): Result<KycStatusResponse, DataError.Network>

    suspend fun getKycStatus(): Result<KycStatusResponse, DataError.Network>

    suspend fun evaluatePassiveLiveness(imageBytes: ByteArray): Result<OpenBiometricsPassiveLivenessResponse, DataError.Network>

    suspend fun searchWatchlist(imageBytes: ByteArray): Result<OpenBiometricsWatchlistSearchResponse, DataError.Network>

    suspend fun getAdminQueue(): Result<List<KycAdminQueueItem>, DataError.Network>

    suspend fun reviewKycSubmission(
        verificationId: String,
        decision: KycStatus,
        notes: String? = null,
    ): Result<KycStatusResponse, DataError.Network>
}
