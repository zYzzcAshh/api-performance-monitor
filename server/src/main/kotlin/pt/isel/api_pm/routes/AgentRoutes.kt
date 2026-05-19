package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.agent.AgentCreateEndpointRequest
import pt.isel.api_pm.dto.agent.AgentRegisterRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.utils.requireUserId

fun Route.agentRoutes(
    agentService: AgentService
) {

    authenticate(AuthConfig.JWT_NAME) {

        post(Routes.Agent.REGISTER) {
            val request =
                call.receive<AgentRegisterRequest>()

            val userId = call.principal<JWTPrincipal>()
                ?.requireUserId() ?: throw MissingTokenException()

            val agent = agentService.register(userId, request.name)

            call.respond(
                HttpStatusCode.Created, agent
            )
        }

        post(Routes.Agent.ENDPOINTS) {
            val request =
                call.receive<AgentCreateEndpointRequest>()

            val userId = call.principal<JWTPrincipal>()
                ?.requireUserId() ?: throw MissingTokenException()

            agentService.addEndpoint(userId, request.agentId, request.name, IntervalSeconds(request.intervalSeconds))
            call.respond(HttpStatusCode.Created, "Endpoint added successfully")
        }
    }
}