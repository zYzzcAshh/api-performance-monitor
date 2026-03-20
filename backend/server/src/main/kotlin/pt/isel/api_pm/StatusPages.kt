package pt.isel.api_pm

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.UserAlreadyExistsException
import pt.isel.api_pm.exceptions.UserNotFoundException

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<UserAlreadyExistsException> { call, cause ->
            call.respond(HttpStatusCode.Conflict, cause.message ?: "User already exists")
        }

        exception<BadCredentialsException> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.message ?: "Invalid username or password")
        }

        exception<UserNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.message ?: "User not found")
        }

        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
        }
    }
}
