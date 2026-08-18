package com.smach.zapmancer.core.framework

import com.smach.zapmancer.core.common.ApiError
import com.smach.zapmancer.core.common.ApiException
import com.smach.zapmancer.core.common.ApiResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.openapi.OpenApiDoc
import io.ktor.openapi.OpenApiInfo
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.metrics.micrometer.MicrometerMetrics
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.requestvalidation.RequestValidation
import io.ktor.server.plugins.requestvalidation.RequestValidationException
import io.ktor.server.plugins.scalar.scalarUI
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.openapi.OpenApiDocSource
import io.ktor.server.routing.openapi.plus
import io.ktor.server.routing.routing
import io.ktor.server.routing.routingRoot
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.micrometer.prometheusmetrics.PrometheusConfig
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import org.slf4j.LoggerFactory
import org.slf4j.event.Level.INFO
import java.util.UUID
import kotlin.time.Duration.Companion.seconds

private val logger = LoggerFactory.getLogger("FrameworkPlugins")

val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)

/**
 * Configures the standard Ktor plugins (DI, Serialization, CORS, Error Handling).
 */
@Suppress("LongMethod")
fun Application.configureFramework(modules: List<Module> = emptyList()) {
    val app = this
    val appModule = org.koin.dsl.module {
        single<Application> { app }
    }
    install(Koin) {
        slf4jLogger()
        modules(listOf(appModule) + modules)
    }

    install(CallId) {
        header(HttpHeaders.XRequestId)
        generate { UUID.randomUUID().toString() }
        verify { callId: String -> callId.isNotEmpty() }
    }

    install(CallLogging) {
        level = INFO
        callIdMdc("requestId")
    }

    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
    }

    install(RateLimit) {
        register(RateLimitName("auth")) {
            rateLimiter(limit = 30, refillPeriod = 60.seconds)
            requestKey { call ->
                call.request.headers["X-Forwarded-For"]?.split(",")?.firstOrNull()?.trim()
                    ?: call.request.headers["CF-Connecting-IP"]
                    ?: "ip_${call.request.headers[HttpHeaders.UserAgent]?.hashCode() ?: 0}"
            }
        }
        register(RateLimitName("otp")) {
            rateLimiter(limit = 5, refillPeriod = 300.seconds)
            requestKey { call ->
                call.request.headers["X-Forwarded-For"]?.split(",")?.firstOrNull()?.trim()
                    ?: call.request.headers["CF-Connecting-IP"]
                    ?: "ip_${call.request.headers[HttpHeaders.UserAgent]?.hashCode() ?: 0}"
            }
        }
    }

    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = 1024 * 1024 // 1 MB max frame size to prevent memory exhaustion
        masking = false
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            },
        )
    }

    val allowedHosts =
        environment.config.propertyOrNull("cors.allowedHosts")?.getList() ?: emptyList()
    install(CORS) {
        if (allowedHosts.isEmpty()) {
            anyHost()
        } else {
            allowedHosts.forEach { host ->
                allowHost(host, schemes = listOf("http", "https"))
            }
        }
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }

    install(RequestValidation) {
    }

    install(StatusPages) {
        exception<RequestValidationException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Unit>(
                    false,
                    error = ApiError("VALIDATION_ERROR", cause.reasons.joinToString()),
                ),
            )
        }

        exception<BadRequestException> { call, cause ->
            logger.warn("Bad request: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Unit>(
                    false,
                    error = ApiError(
                        "BAD_REQUEST",
                        cause.message ?: "Malformed request body or parameters",
                    ),
                ),
            )
        }

        exception<kotlinx.serialization.SerializationException> { call, cause ->
            logger.warn("Serialization error: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Unit>(
                    false,
                    error = ApiError("BAD_REQUEST", cause.message ?: "Invalid request payload format"),
                ),
            )
        }

        exception<IllegalArgumentException> { call, cause ->
            logger.warn("Invalid argument: ${cause.message}")
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Unit>(
                    false,
                    error = ApiError("BAD_REQUEST", cause.message ?: "Invalid argument provided"),
                ),
            )
        }

        exception<ApiException> { call, cause ->
            call.respond(
                status = cause.code.httpStatusCode,
                message = ApiResponse<Unit>(
                    success = false,
                    error = ApiError(
                        code = cause.code.name,
                        message = cause.message,
                    ),
                ),
            )
        }

        exception<Throwable> { call, cause ->
            // Recursively inspect the cause chain for database/SQL exceptions
            val sqlEx = generateSequence(cause) { it.cause }
                .filterIsInstance<org.postgresql.util.PSQLException>()
                .firstOrNull()

            if (sqlEx != null) {
                logger.warn("Database exception [${sqlEx.sqlState}]: ${sqlEx.message}")
                when (sqlEx.sqlState) {
                    "22021", "22P05" -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, error = ApiError("INVALID_ENCODING", "Invalid character encoding in input payload.")),
                    )
                    "22001" -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, error = ApiError("VALUE_TOO_LONG", "Input string exceeds maximum allowed field length.")),
                    )
                    "23503" -> call.respond(
                        HttpStatusCode.NotFound,
                        ApiResponse<Unit>(false, error = ApiError("NOT_FOUND", "Referenced entity does not exist.")),
                    )
                    "23505" -> call.respond(
                        HttpStatusCode.Conflict,
                        ApiResponse<Unit>(false, error = ApiError("CONFLICT", "Resource with this identifier already exists.")),
                    )
                    "23502" -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, error = ApiError("MISSING_FIELD", "Required database field is missing.")),
                    )
                    else -> call.respond(
                        HttpStatusCode.BadRequest,
                        ApiResponse<Unit>(false, error = ApiError("BAD_REQUEST", "Invalid database input format.")),
                    )
                }
                return@exception
            }

            logger.error("Unhandled exception: ${cause.message}", cause)

            val message = if (this@configureFramework.developmentMode) {
                cause.message ?: "Unknown error"
            } else {
                "An internal error occurred. Please try again later."
            }

            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse<Unit>(false, error = ApiError("INTERNAL_ERROR", message)),
            )
        }
    }

    routing {
        get("/openapi.json") {
            val doc = OpenApiDoc(info = OpenApiInfo("My API", "1.0")) + call.application.routingRoot.descendants()
            call.respond(doc)
        }

        openAPI("/openApi")

        swaggerUI("/swaggerUI") {
            info = OpenApiInfo("My API", "1.0")
            source = OpenApiDocSource.Routing(
                contentType = ContentType.Application.Json,
            )
        }

        scalarUI("/scalarUI") {
            info = OpenApiInfo("My API", "1.0.0")
            source = OpenApiDocSource.Routing(
                contentType = ContentType.Application.Json, // or Application.Yaml
            )
            theme = "purple"
            layout = "modern"
        }

        get("/health") {
            call.respondText("OK", ContentType.Text.Plain, HttpStatusCode.OK)
        }

        get("/metrics") {
            call.respondText(
                appMicrometerRegistry.scrape(),
                ContentType.Text.Plain,
                HttpStatusCode.OK,
            )
        }
    }
}
