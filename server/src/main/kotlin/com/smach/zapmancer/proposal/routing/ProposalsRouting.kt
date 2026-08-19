package com.smach.zapmancer.proposal.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.proposal.service.ProposalsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Proposals and Milestone Escrow routing module.
 */
fun Route.proposalsRouting() {
    val service by inject<ProposalsService>()

    authenticate("local-jwt") {
        route("/proposals") {
            /**
             * Submit proposal bid for project
             *
             * Submits a formal freelancer proposal bid for a project posting with cover letter, milestone budget breakdown, and estimated delivery days.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @response 201 Proposal created and submitted. [Proposal]
             * @response 400 Invalid proposal payload or duplicate submission. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<SubmitProposalRequest>()
                val result = service.submitProposal(principal.uid, req)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = result))
            }

            /**
             * Fetch freelancer submitted proposals
             *
             * Retrieves all proposal bids submitted by the authenticated freelancer across all projects.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @response 200 List of submitted proposals. [List<Proposal>]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get("/my") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.getMyProposals(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Accept candidate proposal bid
             *
             * Accepts a freelancer proposal bid and initializes the milestone escrow contract. Restricted to project owner.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @path id The integer proposal identifier.
             * @response 200 Proposal accepted successfully. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 404 Proposal not found. [ApiError]
             */
            post("/{id}/accept") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Proposal not found")),
                    )
                val result = service.acceptProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Decline candidate proposal bid
             *
             * Declines a candidate proposal bid for a project. Restricted to project owner.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @path id The integer proposal identifier.
             * @response 200 Proposal declined successfully. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 404 Proposal not found. [ApiError]
             */
            post("/{id}/reject") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Proposal not found")),
                    )
                val result = service.rejectProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Withdraw submitted proposal
             *
             * Withdraws an active proposal bid from consideration. Restricted to the submitting freelancer.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @path id The integer proposal identifier.
             * @response 200 Proposal withdrawn successfully. [CommonResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 404 Proposal not found. [ApiError]
             */
            post("/{id}/withdraw") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(success = false, error = ApiError("NOT_FOUND", "Proposal not found")),
                    )
                val result = service.withdrawProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }
        }

        route("/projects/{projectId}") {
            /**
             * Fetch proposals submitted for project
             *
             * Retrieves all candidate proposals and bids submitted for a specific project. Restricted to project owner.
             *
             * @tags Proposals & Escrow
             * @security BearerAuth
             * @path projectId The unique project identifier.
             * @response 200 List of candidate proposals. [List<Proposal>]
             * @response 400 Missing project id parameter. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get("/proposals") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val projectId = call.parameters["projectId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val result = service.getProposalsForProject(principal.uid, projectId)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}
