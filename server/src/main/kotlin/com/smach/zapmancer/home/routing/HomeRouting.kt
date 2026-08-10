package com.smach.zapmancer.home.routing

import com.smach.zapmancer.core.common.dto.ExportActivitiesRequest
import com.smach.zapmancer.core.common.dto.ExportActivitiesResponse
import com.smach.zapmancer.core.common.dto.HomeDashboard
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.core.security.UserPrincipal
import com.smach.zapmancer.home.service.HomeService
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

fun Route.homeRouting() {
    val service by inject<HomeService>()

    authenticate("local-jwt") {
        route("/home") {
            /**
             * Fetch authenticated user's dashboard metrics and activity feed.
             *
             * Responses:
             *   – 200 [ApiResponse<HomeDashboard>] Dashboard metrics data.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *   – 404 [ApiResponse<Unit>] User context not found.
             *
             * Tags: Home
             */
            get("/dashboard") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                val result = service.getDashboard(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Export user recent activities to CSV file.
             *
             * Request: [ExportActivitiesRequest] List of activity objects to export
             *
             * Responses:
             *   – 200 [ApiResponse<ExportActivitiesResponse>] CSV file path.
             *   – 401 [ApiResponse<Unit>] Unauthorized.
             *
             * Tags: Home
             */
            post("/activities/export") {
                val request = call.receive<ExportActivitiesRequest>()
                val result = service.exportActivities(request.activities)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}


