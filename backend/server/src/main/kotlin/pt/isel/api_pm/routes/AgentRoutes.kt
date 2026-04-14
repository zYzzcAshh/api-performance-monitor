package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.dto.agent.AgentCreateEndpointRequest
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException

fun Route.agentRoutes() {
    authenticate(AuthConfig.JWT_NAME) {
        post(Routes.Agent.CREATE) {
            val request = call.receive<AgentCreateEndpointRequest>()

            val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
            val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
            val userId = tokenUserId.toUInt()

            call.respondText("Agent endpoint created successfully")
        }
    }
}