package com.smach.zapmancer.landingpage.routing

import com.smach.zapmancer.core.common.dto.LandingPageDto
import com.smach.zapmancer.core.network.ktor.ApiResponse
import com.smach.zapmancer.landingpage.service.LandingPageService
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

/**
 * Public Marketing Landing Page routing module.
 */
fun Route.landingPageRouting() {
    val service by inject<LandingPageService>()

    route("/landing-page") {
        /**
         * Fetch public marketing landing page data
         *
         * Retrieves marketplace hero showcase metrics, supported discipline categories, 3-step escrow walkthrough, and dynamic FAQ answers.
         *
         * @tags Landing Page
         * @response 200 Complete landing page marketing payload. [LandingPageDto]
         */
        get("/data") {
            val result = service.getLandingPageData()
            call.respond(ApiResponse(success = true, data = result))
        }
    }
}
