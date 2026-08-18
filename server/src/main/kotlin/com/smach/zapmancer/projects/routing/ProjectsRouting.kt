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

fun Route.projectsRouting() {
    val service by inject<ProjectsService>()

    authenticate("local-jwt") {
        route("/projects") {
            /**
             * Browse and filter available project postings.
             * Supports both 'limit' and 'pageSize' query parameters for client compatibility.
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
             * Retrieve projects created/posted by the authenticated client.
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
             * Retrieve personalized project recommendations based on user skills.
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
             * Retrieve detailed project specification by ID.
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
             * Update an existing project posting (Client owner only).
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
             * Transition project status (e.g. OPEN, IN_PROGRESS, COMPLETED, CLOSED) (Client owner only).
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
             * Delete or close an existing project posting (Client owner only).
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
             * Save or remove a project from bookmarked/saved list.
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
             * Submit application interest to a project.
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
             * Post a new project (Client mode).
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
