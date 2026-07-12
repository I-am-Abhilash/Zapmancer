package com.smach.zapmancer.projects.routing

import com.smach.zapmancer.common.respondResult
import com.smach.zapmancer.core.common.dto.CreateProjectRequest
import com.smach.zapmancer.core.common.dto.SaveProjectRequest
import com.smach.zapmancer.projects.domain.ProjectsService
import com.smach.zapmancer.security.UserPrincipal
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
            /** GET /projects — project board */
            get {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized
                )
                val query = call.request.queryParameters["query"]
                val category = call.request.queryParameters["category"]
                val sortBy = call.request.queryParameters["sortBy"]
                val page = call.request.queryParameters["page"]?.toIntOrNull()
                val limit = call.request.queryParameters["limit"]?.toIntOrNull()
                call.respondResult(
                    service.getProjects(
                        userId = principal.uid,
                        query = query,
                        category = category,
                        sortBy = sortBy,
                        page = page,
                        limit = limit,
                    ),
                )
            }

            /** GET /projects/{id} */
            get("/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(
                    HttpStatusCode.Unauthorized
                )
                val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)
                call.respondResult(service.getProjectById(id, principal.uid))
            }

            /** POST /projects/{id}/save */
            post("/{id}/save") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized
                )
                val id =
                    call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val req = call.receive<SaveProjectRequest>()
                call.respondResult(service.saveProject(principal.uid, id, req.isSaved))
            }

            /** POST /projects/{id}/apply */
            post("/{id}/apply") {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized
                )
                val id =
                    call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                call.respondResult(service.applyToProject(principal.uid, id))
            }

            /** POST /projects — create new project (client mode) */
            post {
                val principal = call.principal<UserPrincipal>() ?: return@post call.respond(
                    HttpStatusCode.Unauthorized
                )
                val req = call.receive<CreateProjectRequest>()
                call.respondResult(service.createProject(principal.uid, req))
            }
        }
    }
}
