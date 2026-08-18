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

fun Route.proposalsRouting() {
    val service by inject<ProposalsService>()

    authenticate("local-jwt") {
        route("/proposals") {
            /**
             * Submit a proposal bid for a project.
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
             * Retrieve all proposals submitted by the authenticated freelancer.
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
             * Accept a proposal bid (Project Owner only).
             */
            post("/{id}/accept") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Invalid proposal id")),
                    )
                val result = service.acceptProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Reject a proposal bid (Project Owner only).
             */
            post("/{id}/reject") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Invalid proposal id")),
                    )
                val result = service.rejectProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Withdraw a proposal bid (Freelancer owner only).
             */
            post("/{id}/withdraw") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val proposalId = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Invalid proposal id")),
                    )
                val result = service.withdrawProposal(principal.uid, proposalId)
                call.respond(ApiResponse(success = true, data = result))
            }
        }

        route("/projects/{projectId}") {
            /**
             * Retrieve proposals submitted for a specific project (Project Owner only).
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
