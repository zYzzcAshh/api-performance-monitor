package pt.isel.api_pm.routes

import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.dto.agent.AgentCreateEndpointRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.utils.requireUserId

fun Route.agentRoutes() {

    authenticate(AuthConfig.JWT_NAME) {

        post(Routes.Agent.CREATE) {

            val request =
                call.receive<AgentCreateEndpointRequest>()

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            call.respondText(
                "Agent endpoint created successfully"
            )
        }
    }
}