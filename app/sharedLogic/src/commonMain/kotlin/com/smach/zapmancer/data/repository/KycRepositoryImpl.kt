package com.smach.zapmancer.data.repository

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
import com.smach.zapmancer.core.network.ktor.safeApiCall
import com.smach.zapmancer.domain.repository.KycRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.koin.core.annotation.Single

@Single(binds = [KycRepository::class])
class KycRepositoryImpl(
    private val client: HttpClient,
) : KycRepository {

    override suspend fun getReceipt(verificationId: String): Result<KycReceiptResponse, DataError.Network> = safeApiCall<KycReceiptResponse> {
        client.get("kyc/receipt/$verificationId")
    }

    override suspend fun getCapabilities(): Result<OpenBiometricsCapabilitiesResponse, DataError.Network> = safeApiCall<OpenBiometricsCapabilitiesResponse> {
        client.get("kyc/capabilities")
    }

    override suspend fun initializeKyc(
        documentType: KycDocumentType,
        livenessPreset: KycLivenessPreset,
    ): Result<KycInitResponse, DataError.Network> = safeApiCall<KycInitResponse> {
        client.post("kyc/init") {
            setBody(KycInitRequest(documentType = documentType, livenessPreset = livenessPreset))
        }
    }

    override suspend fun submitKyc(
        verificationId: String,
        livenessSessionId: String,
        documentType: KycDocumentType,
        frontBytes: ByteArray,
        backBytes: ByteArray?,
        selfieBytes: ByteArray,
    ): Result<KycStatusResponse, DataError.Network> = safeApiCall<KycStatusResponse> {
        client.submitFormWithBinaryData(
            url = "kyc/submit",
            formData = formData {
                append("verification_id", verificationId)
                append("liveness_session_id", livenessSessionId)
                append("document_type", documentType.name)
                append(
                    "document_front",
                    frontBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"front.jpg\"")
                    },
                )
                if (backBytes != null) {
                    append(
                        "document_back",
                        backBytes,
                        Headers.build {
                            append(HttpHeaders.ContentType, "image/jpeg")
                            append(HttpHeaders.ContentDisposition, "filename=\"back.jpg\"")
                        },
                    )
                }
                append(
                    "selfie",
                    selfieBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"selfie.jpg\"")
                    },
                )
            },
        )
    }

    override suspend fun getKycStatus(): Result<KycStatusResponse, DataError.Network> = safeApiCall<KycStatusResponse> {
        client.get("kyc/status")
    }

    override suspend fun evaluatePassiveLiveness(
        imageBytes: ByteArray,
    ): Result<OpenBiometricsPassiveLivenessResponse, DataError.Network> = safeApiCall<OpenBiometricsPassiveLivenessResponse> {
        client.submitFormWithBinaryData(
            url = "kyc/passive-liveness",
            formData = formData {
                append(
                    "image",
                    imageBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    },
                )
            },
        )
    }

    override suspend fun searchWatchlist(
        imageBytes: ByteArray,
    ): Result<OpenBiometricsWatchlistSearchResponse, DataError.Network> = safeApiCall<OpenBiometricsWatchlistSearchResponse> {
        client.submitFormWithBinaryData(
            url = "kyc/watchlists/search",
            formData = formData {
                append(
                    "image",
                    imageBytes,
                    Headers.build {
                        append(HttpHeaders.ContentType, "image/jpeg")
                        append(HttpHeaders.ContentDisposition, "filename=\"image.jpg\"")
                    },
                )
            },
        )
    }

    override suspend fun getAdminQueue(): Result<List<KycAdminQueueItem>, DataError.Network> = safeApiCall<List<KycAdminQueueItem>> {
        client.get("kyc/admin/queue")
    }

    override suspend fun reviewKycSubmission(
        verificationId: String,
        decision: KycStatus,
        notes: String?,
    ): Result<KycStatusResponse, DataError.Network> = safeApiCall<KycStatusResponse> {
        client.post("kyc/admin/$verificationId/review") {
            setBody(KycAdminReviewRequest(decision = decision, notes = notes))
        }
    }
}
