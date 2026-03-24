package pt.isel.api_pm.configure

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import pt.isel.api_pm.exceptions.ForbiddenException

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt("auth-jwt") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256("api-pm-secret"))
                    .withIssuer("api-pm")
                    .build(),
            )

            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                userId ?.let {JWTPrincipal(credential.payload)}
            }

            challenge { _, _ ->
                throw ForbiddenException()
            }
        }
    }
}
