package com.smach.zapmancer.projects.routing

import com.smach.zapmancer.core.common.CommonResponse
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.Project
import com.smach.zapmancer.core.common.dto.ProjectDetail
import com.smach.zapmancer.core.common.dto.SaveProjectRequest
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.projects.service.ProjectsService
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

fun Route.projectsRouting() {
    val service by inject<ProjectsService>()

    authenticate("local-jwt") {
        route("/projects") {
            /**
             * Browse and filter available project postings.
             *
             * Query: query [String] Optional search keyword
             * Query: category [String] Filter by category
             * Query: sortBy [String] Sorting order
             * Query: page [Int] Page index
             * Query: limit [Int] Number of items per page
             *
             * Responses:
             *   – 200 [ApiResponse<List<Project>>] List of matching project summaries.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Projects
             */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val query = call.request.queryParameters["query"]
                val category = call.request.queryParameters["category"]
                val sortBy = call.request.queryParameters["sortBy"]
                val page = call.request.queryParameters["page"]?.toIntOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()
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
             * Retrieve detailed project specification by ID.
             *
             * Path: id [String] Project ID
             *
             * Responses:
             *   – 200 [ApiResponse<ProjectDetail>] Detailed project information.
             *   – 404 [ApiResponse<Unit>] Project not found.
             *
             * Tags: Projects
             */
            get("/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                val result = service.getProjectById(id, principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Save or remove a project from bookmarked/saved list.
             *
             * Path: id [String] Project ID
             * Request: [SaveProjectRequest] Toggle status
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Saved status toggled.
             *
             * Tags: Projects
             */
            post("/{id}/save") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val id =
                    call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val req = call.receive<SaveProjectRequest>()
                val result = service.saveProject(principal.uid, id, req.isSaved)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Submit application interest to a project.
             *
             * Path: id [String] Project ID
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Application submitted.
             *   – 404 [ApiResponse<Unit>] Project not found.
             *
             * Tags: Projects
             */
            post("/{id}/apply") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val id =
                    call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val result = service.applyToProject(principal.uid, id)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Post a new project (Client mode).
             *
             * Request: [CreateProjectRequest] Project creation parameters
             *
             * Responses:
             *   – 200 [ApiResponse<CommonResponse>] Project created successfully.
             *
             * Tags: Projects
             */
            post {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized,
                )
                val req = call.receive<CreateProjectRequest>()
                val result = service.createProject(principal.uid, req)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}


