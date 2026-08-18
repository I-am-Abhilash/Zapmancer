package com.smach.zapmancer.kyc.routing

import com.smach.zapmancer.core.common.dto.KycAdminQueueItem
import com.smach.zapmancer.core.common.dto.KycAdminReviewRequest
import com.smach.zapmancer.core.common.dto.KycDocumentType
import com.smach.zapmancer.core.common.dto.KycInitRequest
import com.smach.zapmancer.core.common.dto.KycInitResponse
import com.smach.zapmancer.core.common.dto.KycReceiptResponse
import com.smach.zapmancer.core.common.dto.KycStatusResponse
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

fun Route.kycRouting() {
    val kycService by inject<KycService>()

    route("/kyc") {
        // -------------------------------------------------------------------
        // Public Endpoints
        // -------------------------------------------------------------------

        /**
         * Offline-verifiable public receipt verification for legal and contract audits.
         */
        get("/receipt/{id}") {
            val verificationId = call.parameters["id"]
                ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing verification id"))
                )
            val receipt = kycService.getReceipt(verificationId)
                ?: return@get call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Receipt not found"))
                )

            call.respond(ApiResponse(success = true, data = receipt))
        }

        /**
         * Retrieve active OpenBiometrics capabilities and supported presets.
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
             * Initialize KYC verification and receive active liveness challenge.
             */
            post("/init") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val request = call.receive<KycInitRequest>()
                val response = kycService.initializeKyc(principal.uid, request)
                call.respond(ApiResponse(success = true, data = response))
            }

            /**
             * Submit ID documents and live video selfie for AI verification and Ed25519 signing.
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
                            error = ApiError("BAD_REQUEST", "Missing required fields (verification_id, document_front, or selfie)")
                        )
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
                    registeredUsername = principal.uid
                )

                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Retrieve latest verification status for the authenticated user.
             */
            get("/status") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val status = kycService.getKycStatus(principal.uid)
                    ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "No KYC record found"))
                    )

                call.respond(ApiResponse(success = true, data = status))
            }

            /**
             * Single-frame passive liveness evaluation (anti-spoofing).
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
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing image file"))
                )

                val result = kycService.evaluatePassiveLiveness(bytes)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * 1:N Fraud watchlist face search.
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
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing image file"))
                )

                val result = kycService.searchWatchlist(bytes)
                call.respond(ApiResponse(success = true, data = result))
            }

            // ---------------------------------------------------------------
            // Admin Moderation Endpoints
            // ---------------------------------------------------------------

            /**
             * Get list of pending verifications requiring manual review.
             */
            get("/admin/queue") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val queue = kycService.getAdminQueue()
                call.respond(ApiResponse(success = true, data = queue))
            }

            /**
             * Submit a manual review decision.
             */
            post("/admin/{id}/review") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(HttpStatusCode.Unauthorized)
                val verificationId = call.parameters["id"]
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing verification id"))
                    )
                val request = call.receive<KycAdminReviewRequest>()

                val updated = kycService.reviewKycSubmission(
                    verificationId = verificationId,
                    adminUserId = principal.uid,
                    decision = request.decision,
                    notes = request.notes
                ) ?: return@post call.respond(
                    HttpStatusCode.NotFound,
                    ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Verification not found"))
                )

                call.respond(ApiResponse(success = true, data = updated))
            }
        }
    }
}
