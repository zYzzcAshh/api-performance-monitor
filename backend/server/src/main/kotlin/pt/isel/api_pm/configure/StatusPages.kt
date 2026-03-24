package pt.isel.api_pm.configure

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.plugins.statuspages.StatusPagesConfig
import io.ktor.server.response.respond
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.DuplicateEndpointException
import pt.isel.api_pm.exceptions.ForbiddenException
import pt.isel.api_pm.exceptions.InvalidIntervalException
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.InvalidUrlException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
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
        on<InvalidUrlException>(HttpStatusCode.BadRequest)
        on<InvalidIntervalException>(HttpStatusCode.BadRequest)
        on<IllegalArgumentException>(HttpStatusCode.BadRequest)

        exception<Throwable> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, "An unexpected error occurred: ${cause.message}")
        }
    }
}
