package com.smach.zapmancer.home.routing

import com.smach.zapmancer.common.respondResult
import com.smach.zapmancer.core.common.dto.ExportActivitiesRequest
import com.smach.zapmancer.home.domain.HomeService
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

fun Route.homeRouting() {
    val service by inject<HomeService>()

    authenticate("local-jwt") {
        route("/home") {
            /** GET /home/dashboard */
            get("/dashboard") {
                val principal = call.principal<UserPrincipal>()
                    ?: return@get call.respond(HttpStatusCode.Unauthorized)
                call.respondResult(service.getDashboard(principal.uid))
            }

            /** POST /home/activities/export */
            post("/activities/export") {
                val request = call.receive<ExportActivitiesRequest>()
                call.respondResult(service.exportActivities(request.activities))
            }
        }
    }
}
