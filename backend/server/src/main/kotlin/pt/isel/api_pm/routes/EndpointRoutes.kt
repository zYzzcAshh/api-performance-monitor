package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.dto.CreateEndpointRequest
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.service.EndpointService
import kotlin.time.Clock

fun Route.endpointRoutes(service: EndpointService) {

    route("/api/endpoints") {

        authenticate("auth-jwt") {

            get {
                val principal = call.principal<JWTPrincipal>()!!
                val userId = principal.getClaim("userId", Int::class)
                    ?: throw InvalidTokenException()

                call.respond(service.getByUser(userId))
            }

            post {
                val request = call.receive<CreateEndpointRequest>()
                val principal = call.principal<JWTPrincipal>()!!

                val userId = principal.getClaim("userId", Int::class)
                    ?: throw InvalidTokenException()

                val endpoint = MonitoredEndpoint(
                    id = 0,
                    userId = userId,
                    url = request.url,
                    name = request.name,
                    intervalSeconds = request.intervalSeconds,
                    createdAt = Clock.System.now()
                )

                service.add(endpoint)

                call.respond(endpoint)
            }

            delete("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                service.delete(id)
                call.respond("Deleted")
            }
        }
    }
}