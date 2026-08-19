package com.smach.zapmancer.home.routing

import com.smach.zapmancer.core.common.dto.ExportActivitiesRequest
import com.smach.zapmancer.core.common.dto.ExportActivitiesResponse
import com.smach.zapmancer.core.common.dto.HomeDashboard
import com.smach.zapmancer.core.network.ktor.ApiError
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

/**
 * Talent Workspace Dashboard and Activity Logs routing module.
 */
fun Route.homeRouting() {
    val service by inject<HomeService>()

    authenticate("local-jwt") {
        route("/home") {
            /**
             * Fetch talent workspace dashboard metrics
             *
             * Retrieves weekly earnings, active contracts count, hours logged, and real-time recent project activities for the authenticated user.
             *
             * @tags Talent Workspace
             * @security BearerAuth
             * @response 200 Dashboard metrics and activity telemetry. [HomeDashboard]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            get("/dashboard") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                val result = service.getDashboard(principal.uid)
                call.respond(ApiResponse(success = true, data = result))
            }

            /**
             * Export activity logs to CSV format
             *
             * Exports recent work sessions and billing activities to a downloadable CSV spreadsheet file.
             *
             * @tags Talent Workspace
             * @security BearerAuth
             * @response 200 CSV file generated successfully. [ExportActivitiesResponse]
             * @response 401 Missing or invalid authentication token. [ApiError]
             */
            post("/activities/export") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@post call.respond(
                        HttpStatusCode.Unauthorized,
                        ApiResponse<Unit>(success = false, error = ApiError("UNAUTHORIZED", "Missing or invalid token")),
                    )
                val request = call.receive<ExportActivitiesRequest>()
                val result = service.exportActivities(request.activities)
                call.respond(ApiResponse(success = true, data = result))
            }
        }
    }
}
