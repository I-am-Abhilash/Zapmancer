package com.smach.zapmancer.landingpage.routing

import com.smach.zapmancer.core.common.dto.LandingPageDto
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.landingpage.service.LandingPageService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.landingPageRouting() {
    val service by inject<LandingPageService>()

    route("/landing-page") {
        /**
         * Retrieve public landing page marketing content and showcase metrics.
         *
         * Responses:
         *   – 200 [ApiResponse<LandingPageDto>] Complete landing page payload.
         *
         * Tags: Landing Page
         */
        get("/data") {
            val result = service.getLandingPageData()
            call.respond(ApiResponse(success = true, data = result))
        }
    }
}
