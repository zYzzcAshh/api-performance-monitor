package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.endpoint.CreateEndpointRequest
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.EndpointService

fun Route.endpointRoutes(service: EndpointService) {
    route(Routes.Endpoints.BASE) {
        authenticate(AuthConfig.JWT_NAME) {
            post(Routes.Endpoints.CREATE) {
                val request = call.receive<CreateEndpointRequest>()

                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()

                val url = EndpointUrl(request.url)
                val interval = IntervalSeconds(request.intervalSeconds)

                service.add(tokenUserId, url, request.name, interval)

                call.respondText("Endpoint created successfully", status = HttpStatusCode.Created)
            }

            get {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()

                call.respond(service.getByUser(tokenUserId))
            }

            delete(Routes.Endpoints.DELETE) {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()

                val id = call.parameters["id"]!!.toInt()
                service.delete(tokenUserId, id)
                call.respond("Deleted")
            }
        }
    }
}
