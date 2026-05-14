package pt.isel.api_pm.configure

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.exceptions.ForbiddenException

fun Application.configureAuthentication() {
    install(Authentication) {
        jwt(AuthConfig.JWT_NAME) {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(AuthConfig.SECRET))
                    .withIssuer(AuthConfig.ISSUER)
                    .build(),
            )

            validate { credential ->
                val userId =
                    credential.payload
                        .getClaim(AuthConfig.USER_ID_CLAIM)
                        .asInt()

                userId?.let { JWTPrincipal(credential.payload) }
            }

            challenge { _, _ ->
                throw ForbiddenException()
            }
        }
    }
}
