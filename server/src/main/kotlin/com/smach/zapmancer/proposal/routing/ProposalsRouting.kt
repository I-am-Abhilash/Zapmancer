package com.smach.zapmancer.proposal.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.Proposal
import com.smach.zapmancer.core.common.dto.SubmitProposalRequest
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
        /**
         * Submit a proposal bid for a project.
         *
         * Request: [SubmitProposalRequest] Proposal parameters
         *
         * Responses:
         *   – 200 [ApiResponse<CommonResponse>] Proposal submitted successfully.
         *   – 401 [ApiResponse<Unit>] Unauthorized.
         *
         * Tags: Proposals
         */
        post("/proposals") {
            val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                HttpStatusCode.Unauthorized,
                "Missing or invalid token",
            )
            val req = call.receive<SubmitProposalRequest>()
            val result = service.submitProposal(principal.uid, req)
            call.respond(ApiResponse(success = true, data = result))
        }

        route("/projects/{projectId}") {
            /**
             * Retrieve proposals submitted for a specific project (Client view).
             *
             * Path: projectId [String] Target project ID
             *
             * Responses:
             *   – 200 [ApiResponse<List<Proposal>>] List of submitted proposals.
             *   – 400 [ApiResponse<Unit>] Missing project ID.
             *
             * Tags: Proposals
             */
            get("/proposals") {
                val projectId = call.parameters["projectId"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    "Project ID is missing from the URL",
                )
                val result = service.getProposalsForProject(projectId)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}


