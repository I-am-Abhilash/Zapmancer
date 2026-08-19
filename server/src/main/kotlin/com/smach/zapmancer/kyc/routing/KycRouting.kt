package com.smach.zapmancer.kyc.routing

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycAdminReviewRequest
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitRequest
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.dto.KycStatusResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsCapabilitiesResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsPassiveLivenessResponse
import com.smach.zapmancer.core.common.dto.OpenBiometricsWatchlistSearchResponse
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.kyc.service.KycService
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.http.content.streamProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import org.koin.ktor.ext.inject

/**
 * OpenBiometrics KYC verification and audit routing module.
 */
fun Route.kycRouting() {
    val kycService by inject<KycService>()

    route("/kyc") {
        // -------------------------------------------------------------------
        // Public Endpoints
        // -------------------------------------------------------------------

        /**
         * Fetch public cryptographic KYC audit receipt
         *
         * Retrieves the offline-verifiable Ed25519 digital signature and canonical hash proof for third-party legal, contract, and escrow audits.
         *
         * @tags OpenBiometrics KYC
         * @path id The unique verification session identifier.
         * @response 200 Cryptographic verification receipt. [KycReceiptResponse]
         * @response 400 Missing verification id. [ApiError]
         * @response 404 Verification receipt not found. [ApiError]
         */
        get("/receipt/{id}") {
            val verificationId = call.parameters["id"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing verification id")),
                )
            val receipt = kycService.getReceipt(verificationId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Receipt not found")),
                )

            call.respond(ApiResponse(success = true, data = receipt))
        }

        /**
         * Fetch OpenBiometrics active engine capabilities
         *
         * Queries supported biometric challenge presets (EYE, HEAD_TURN, SMILE) and accepted government document types.
         *
         * @tags OpenBiometrics KYC
         * @response 200 Engine capabilities and supported presets. [OpenBiometricsCapabilitiesResponse]
         */
        get("/capabilities") {
            val capabilities = kycService.getCapabilities()
            call.respond(ApiResponse(success = true, data = capabilities))
        }

        // -------------------------------------------------------------------
        // Authenticated Endpoints
        // -------------------------------------------------------------------

        authenticate("local-jwt") {
            /**
             * Initialize KYC verification session
             *
             * Starts an identity verification session and retrieves an active liveness challenge motion instruction.
             *
             * @tags OpenBiometrics KYC
             * @security BearerAuth
             * @response 200 Session initialized with active challenge. [KycInitResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/init") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val request = call.receive<KycInitRequest>()
                val response = kycService.initializeKyc(principal.uid, request)
                call.respond(ApiResponse(success = true, data = response))
            }

            /**
             * Submit multipart KYC documents and live selfie
             *
             * Processes multi-part binary payload with front/back government ID scans and live selfie video frame for neural anti-spoofing and OCR extraction.
             *
             * @tags OpenBiometrics KYC
             * @security BearerAuth
             * @response 200 Verification processed with confidence score. [KycStatusResponse]
             * @response 400 Missing required multipart fields or invalid images. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/submit") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val multipart = call.receiveMultipart()
                var verificationId = ""
                var livenessSessionId = ""
                var documentType = KycDocumentType.PASSPORT
                var frontBytes: ByteArray? = null
                var backBytes: ByteArray? = null
                var selfieBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    when (part) {
                        is PartData.FormItem -> {
                            when (part.name) {
                                "verification_id" -> verificationId = part.value

                                "liveness_session_id" -> livenessSessionId = part.value

                                "document_type" -> {
                                    documentType = try {
                                        KycDocumentType.valueOf(part.value.uppercase())
                                    } catch (_: Exception) {
                                        KycDocumentType.PASSPORT
                                    }
                                }
                            }
                        }

                        is PartData.FileItem -> {
                            val bytes = part.provider().readRemaining().readByteArray()
                            when (part.name) {
                                "document_front" -> frontBytes = bytes
                                "document_back" -> backBytes = bytes
                                "selfie" -> selfieBytes = bytes
                            }
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                if (verificationId.isBlank() || frontBytes == null || selfieBytes == null) {
                    return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(
                            success = false,
                            error = ApiError("BAD_REQUEST", "Missing required fields (verification_id, document_front, or selfie)"),
                        ),
                    )
                }

                val result = kycService.processKycSubmission(
                    userId = principal.uid,
                    verificationId = verificationId,
                    livenessSessionId = livenessSessionId,
                    documentType = documentType,
                    frontBytes = frontBytes!!,
                    backBytes = backBytes,
                    selfieBytes = selfieBytes!!,
                    registeredUsername = principal.uid,
                )

                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Fetch user KYC verification status
             *
             * Retrieves the latest verification status, match similarity score, and cryptographic audit signature for the authenticated user.
             *
             * @tags OpenBiometrics KYC
             * @security BearerAuth
             * @response 200 Latest KYC status and confidence scores. [KycStatusResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 404 No KYC record found. [ApiError]
             */
            get("/status") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val status = kycService.getKycStatus(principal.uid)
                    ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "No KYC record found")),
                    )

                call.respond(ApiResponse(success = true, data = status))
            }

            /**
             * Evaluate passive liveness anti-spoofing
             *
             * Performs single-frame neural anti-spoofing evaluation on a submitted face image.
             *
             * @tags OpenBiometrics KYC
             * @security BearerAuth
             * @response 200 Passive liveness confidence score. [OpenBiometricsPassiveLivenessResponse]
             * @response 400 Missing image binary payload. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/passive-liveness") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val multipart = call.receiveMultipart()
                var imageBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem && part.name == "image") {
                        imageBytes = part.provider().readRemaining().readByteArray()
                    }
                    part.dispose()
                }

                val bytes = imageBytes ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing image file")),
                )

                val result = kycService.evaluatePassiveLiveness(bytes)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Screen face against fraud watchlist
             *
             * Performs 1:N biometric face search against known identity fraud and watchlist databases.
             *
             * @tags OpenBiometrics KYC
             * @security BearerAuth
             * @response 200 Watchlist screening result. [OpenBiometricsWatchlistSearchResponse]
             * @response 400 Missing image binary payload. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/watchlists/search") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)

                val multipart = call.receiveMultipart()
                var imageBytes: ByteArray? = null

                multipart.forEachPart { part ->
                    if (part is PartData.FileItem && part.name == "image") {
                        imageBytes = part.provider().readRemaining().readByteArray()
                    }
                    part.dispose()
                }

                val bytes = imageBytes ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing image file")),
                )

                val result = kycService.searchWatchlist(bytes)
                call.respond(ApiResponse(success = true, data = result))
            }

            // ---------------------------------------------------------------
            // Admin Moderation Endpoints
            // ---------------------------------------------------------------

            /**
             * Fetch pending KYC moderation queue
             *
             * Retrieves the list of verification submissions flagged for manual human review. Restricted to administrators.
             *
             * @tags OpenBiometrics KYC, Admin Operations
             * @security BearerAuth
             * @response 200 Queue of pending verification submissions. [List<KycAdminQueueItem>]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 403 Admin privileges required. [ApiError]
             */
            get("/admin/queue") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                if (principal.role != "ADMIN") {
                    return@get call.respond(
                        HttpStatusCode.Forbidden,
                        ApiResponse<Unit>(success = false, error = ApiError("FORBIDDEN", "Admin privileges required.")),
                    )
                }
                val queue = kycService.getAdminQueue()
                call.respond(ApiResponse(success = true, data = queue))
            }

            /**
             * Submit KYC manual review decision
             *
             * Records an administrative approval or rejection decision with reviewer audit notes. Restricted to administrators.
             *
             * @tags OpenBiometrics KYC, Admin Operations
             * @security BearerAuth
             * @path id The unique verification session identifier.
             * @response 200 Updated verification status. [KycStatusResponse]
             * @response 400 Missing verification id. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 403 Admin privileges required. [ApiError]
             * @response 404 Verification record not found. [ApiError]
             */
            post("/admin/{id}/review") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                if (principal.role != "ADMIN") {
                    return@post call.respond(
                        HttpStatusCode.Forbidden,
                        ApiResponse<Unit>(success = false, error = ApiError("FORBIDDEN", "Admin privileges required.")),
                    )
                }
                val verificationId = call.parameters["id"]
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing verification id")),
                    )
                val request = call.receive<KycAdminReviewRequest>()

                val updated = kycService.reviewKycSubmission(
                    verificationId = verificationId,
                    adminUserId = principal.uid,
                    decision = request.decision,
                    notes = request.notes,
                ) ?: return@post call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Verification not found")),
                )

                call.respond(ApiResponse(success = true, data = updated))
            }
        }
    }
}
