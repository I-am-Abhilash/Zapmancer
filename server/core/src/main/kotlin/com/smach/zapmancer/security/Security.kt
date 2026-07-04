package com.smach.zapmancer.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond
import java.util.Date

/**
 * UserPrincipal holds the authenticated user's identity extracted from the JWT.
 */
class UserPrincipal(val uid: String, val email: String?)

data class Tokens(val accessToken: String, val refreshToken: String)

/**
 * Helper to generate and verify locally-issued JWT tokens.
 */
object JwtConfig {
    private var secret: String = "zapmancer-default-dev-secret"
    const val ISSUER = "zapmancer-backend"
    const val AUDIENCE = "zapmancer-users"

    private const val ACCESS_TOKEN_VALIDITY_MS = 3_600_000L          // 1 hour
    private const val REFRESH_TOKEN_VALIDITY_MS = 36_000_000L * 30L  // 30 days

    private var algorithm = Algorithm.HMAC256(secret)

    fun initialize(secret: String) {
        this.secret = secret
        this.algorithm = Algorithm.HMAC256(secret)
    }

    fun getAlgorithm(): Algorithm = algorithm

    fun generateTokens(uid: String, email: String): Tokens {
        val accessToken = JWT.create()
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("user_id", uid)
            .withClaim("email", email)
            .withClaim("type", "access")
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY_MS))
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject("Authentication")
            .withIssuer(ISSUER)
            .withAudience(AUDIENCE)
            .withClaim("user_id", uid)
            .withClaim("type", "refresh")
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY_MS))
            .sign(algorithm)

        return Tokens(accessToken, refreshToken)
    }

    fun verifyRefreshToken(token: String): String? {
        return try {
            val verifier = JWT.require(algorithm)
                .withIssuer(ISSUER)
                .withAudience(AUDIENCE)
                .withClaim("type", "refresh")
                .build()
            verifier.verify(token).getClaim("user_id").asString()
        } catch (_: Exception) {
            null
        }
    }
}

/**
 * Configures Ktor Authentication. Only uses the locally-issued JWT provider
 * ("local-jwt"). Firebase/Google JWKS is not needed for Zapmancer.
 * Local JWT Provider ("local-jwt") – verifies tokens issued by /auth/login
 * and /auth/register. All authenticated endpoints use this provider.
 */

fun Application.configureSecurity() {
    val localSecret =
        environment.config.propertyOrNull("jwt.localSecret")?.getString()
            ?: "zapmancer-default-dev-secret"
    JwtConfig.initialize(localSecret)
    install(Authentication) {
        jwt("local-jwt") {
            realm = "Zapmancer"
            verifier(
                JWT.require(JwtConfig.getAlgorithm())
                    .withIssuer(JwtConfig.ISSUER)
                    .withAudience(JwtConfig.AUDIENCE)
                    .withClaim("type", "access")
                    .build()
            )
            validate { credential ->
                val userId = credential.payload.getClaim("user_id").asString()
                val email = credential.payload.getClaim("email").asString()
                if (userId != null) UserPrincipal(userId, email) else null
            }
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "Invalid or expired token"
                )
            }
        }
    }
}
