package pt.isel.api_pm.configure

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import pt.isel.api_pm.exceptions.*
import javax.naming.AuthenticationException

private inline fun <reified T: Throwable> StatusPagesConfig.on(statusCode: HttpStatusCode) {
    exception<T> { call, cause ->
        call.respond(statusCode, checkNotNull(cause.message))
    }
}

fun Application.configureStatusPages() {
    install(StatusPages) {
        on<RegistrationFailedException>(HttpStatusCode.Conflict)
        on<UserNotFoundException>(HttpStatusCode.NotFound)
        on<BadCredentialsException>(HttpStatusCode.Unauthorized)
        on<AuthenticationException>(HttpStatusCode.Unauthorized)
        on<InvalidTokenException>(HttpStatusCode.Unauthorized)
        on<ForbiddenException>(HttpStatusCode.Forbidden)
        on<DuplicateEndpointException>(HttpStatusCode.Conflict)
        on<InvalidIntervalException>(HttpStatusCode.BadRequest)
        on<IllegalArgumentException>(HttpStatusCode.BadRequest)

        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
        }
    }
}
