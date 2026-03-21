package pt.isel.api_pm.routes

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.dto.CreateEndpointRequest
import pt.isel.api_pm.service.EndpointService
import kotlin.time.Clock

fun Route.endpointRoutes(service: EndpointService) {

    route("/api/endpoints") {

        get {
            call.respond(service.getAll())
        }

        post {
            val request = call.receive<CreateEndpointRequest>()

            val endpoint = MonitoredEndpoint(
                id = 0, // só para ser simples agora
                userId = 0, // depois temos de ligar ao JWT
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