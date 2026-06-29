package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.dto.agent.AgentCreateEndpointRequest
import pt.isel.api_pm.dto.agent.AgentRegisterRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.utils.requireAgentId
import pt.isel.api_pm.utils.requireUserId

fun Route.agentRoutes(
    agentService: AgentService,
    sessionManager: AgentSessionManager
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

            val agentId = call.principal<JWTPrincipal>()
                ?.requireAgentId() ?: throw MissingTokenException()

            agentService.addEndpoint(userId, agentId, request.name, request.method, IntervalSeconds(request.intervalSeconds))
            call.respond(HttpStatusCode.Created, "Endpoint added successfully")
        }

        webSocket("ws/agent") {
            val userId = call.principal<JWTPrincipal>()
                ?.requireUserId() ?: throw MissingTokenException()

            val agentId = call.principal<JWTPrincipal>()
                ?.requireAgentId() ?: throw MissingTokenException()

            sessionManager.register(userId, agentId, this)
            try {
                for (frame in incoming) {
                    if (frame is Frame.Text) sessionManager.handleIncoming(userId, agentId, frame)
                }
            } finally {
                sessionManager.unregister(userId, agentId) // runs on disconnect, including network drops
            }
        }
    }
}