package pt.isel.api_pm.configure

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.ForbiddenException
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
import javax.naming.AuthenticationException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<RegistrationFailedException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "User already exists")
        }

        exception<BadCredentialsException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Invalid username or password")
        }

        exception<UserNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "User not found")
        }

        exception<AuthenticationException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Authentication failed")
        }

        exception<InvalidTokenException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Invalid token")
        }

        exception<InvalidTokenException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Invalid token")
        }

        exception<ForbiddenException> { call, cause ->
            call.respond(HttpStatusCode.Forbidden, cause.message ?: "Access denied")
        }

        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
        }
    }
}
