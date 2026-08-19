package com.smach.zapmancer.projects.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.core.common.dto.SaveProjectRequest
import com.smach.zapmancer.core.common.dto.UpdateProjectRequest
import com.smach.zapmancer.core.common.dto.UpdateProjectStatusRequest
import com.smach.zapmancer.core.network.ktor.ApiError
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.projects.service.ProjectsService
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Projects and Bounties marketplace routing module.
 */
fun Route.projectsRouting() {
    val service by inject<ProjectsService>()

    authenticate("local-jwt") {
        route("/projects") {
            /**
             * Browse and search projects
             *
             * Fetches a paginated list of project contracts with filtering by keyword search, category, and sorting criteria.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @query query Keyword search string for project title or description.
             * @query category Category filter (e.g. Mobile, Backend, AI).
             * @query sortBy Sorting order (e.g. newest, budget_high, budget_low).
             * @query page The page index to fetch (1-indexed).
             * @query limit Maximum number of project records per page.
             * @response 200 Paginated list of projects. [List<Project>]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val query = call.request.queryParameters["query"]
                val category = call.request.queryParameters["category"]
                val sortBy = call.request.queryParameters["sortBy"]
                val page = call.request.queryParameters["page"]?.toIntOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()
                    ?: call.request.queryParameters["pageSize"]?.toIntOrNull()
                val result = service.getProjects(
                    userId = principal.uid,
                    query = query,
                    category = category,
                    sortBy = sortBy,
                    page = page,
                    limit = limit,
                )
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Fetch projects created by authenticated client
             *
             * Retrieves all active, draft, and completed projects posted by the authenticated client.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @response 200 List of client posted projects. [List<Project>]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get("/my") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val result = service.getMyProjects(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Fetch recommended projects for freelancer
             *
             * Generates personalized project contract recommendations based on the user's verified skills and past contract history.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @query limit Maximum number of recommendations to return.
             * @response 200 List of recommended projects. [List<Project>]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get("/recommended") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()
                    ?: call.request.queryParameters["pageSize"]?.toIntOrNull()
                    ?: 10
                val result = service.getRecommendedProjects(userId = principal.uid, limit = limit)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Fetch project details by ID
             *
             * Retrieves the full specification, deliverables, milestones, and proposal count for a single project posting.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Detailed project specification. [ProjectDetail]
             * @response 400 Missing project id parameter. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             * @response 404 Project record not found. [ApiError]
             */
            get("/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@get call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val result = service.getProjectById(id, principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Update project posting
             *
             * Modifies the title, description, budget, category, or milestone list of an existing project posting. Restricted to the project owner.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Project updated successfully. [ProjectDetail]
             * @response 400 Invalid update payload or missing id. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            put("/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@put call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val req = call.receive<UpdateProjectRequest>()
                val result = service.updateProject(principal.uid, id, req)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Update project status
             *
             * Transitions the lifecycle status of a project posting (e.g. OPEN, IN_PROGRESS, COMPLETED, CANCELLED). Restricted to project owner.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Project status updated successfully. [CommonResponse]
             * @response 400 Missing id or invalid status value. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/{id}/status") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val req = call.receive<UpdateProjectStatusRequest>()
                val result = service.updateProjectStatus(principal.uid, id, req.status)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Delete project posting
             *
             * Permanently deletes or cancels an open project contract. Restricted to project owner.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Project deleted successfully. [CommonResponse]
             * @response 400 Missing project id parameter. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            delete("/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@delete call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@delete call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val result = service.deleteProject(principal.uid, id)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Bookmark or un-save project
             *
             * Toggles the saved bookmark status of a project for the authenticated user.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Bookmark state updated successfully. [CommonResponse]
             * @response 400 Missing project id parameter. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/{id}/save") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val req = call.receive<SaveProjectRequest>()
                val result = service.saveProject(principal.uid, id, req.isSaved)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Submit quick application interest
             *
             * Expresses one-click application interest on a project bounty before submitting full proposal details.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @path id The unique project identifier.
             * @response 200 Application interest registered. [CommonResponse]
             * @response 400 Missing project id parameter. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/{id}/apply") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val id = call.parameters["id"] ?: return@post call.respond(
                    HttpStatusCode.BadRequest,
                    ApiResponse<Unit>(success = false, error = ApiError("BAD_REQUEST", "Missing project id")),
                )
                val result = service.applyToProject(principal.uid, id)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Post a new project bounty
             *
             * Creates and publishes a new project contract bounty on the marketplace with milestone breakdown and budget requirements.
             *
             * @tags Projects & Bounties
             * @security BearerAuth
             * @response 201 Project created and published successfully. [ProjectDetail]
             * @response 400 Validation failure or missing required fields. [ApiError]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                )
                val req = call.receive<CreateProjectRequest>()
                val result = service.createProject(principal.uid, req)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = result))
            }
        }
    }
}
