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
import pt.isel.api_pm.dto.endpoint.toDTO
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.EndpointService

fun Route.endpointRoutes(service: EndpointService) {
    authenticate(AuthConfig.JWT_NAME) {

        post(Routes.Endpoints.BASE) {
            val request = call.receive<CreateEndpointRequest>()

            println("REQUEST = $request")
            println("NOTIFICATION = ${request.notification}")
            println("ALERT = ${request.alertRule}")

            val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
            val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
            val userId = tokenUserId.toUInt()

            val url = EndpointUrl(request.url)
            val interval = IntervalSeconds(request.intervalSeconds)

            service.add(userId, url, request.name, interval, request.notification, request.alertRule)

            call.respond(HttpStatusCode.Created)
        }

        get(Routes.Endpoints.BASE) {
            val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
            val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
            val userId = tokenUserId.toUInt()

            val endpoints = service.getByUser(userId)

            call.respond(endpoints.toDTO())
        }

        delete(Routes.Endpoints.DELETE) {
            val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
            val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
            val userId = tokenUserId.toUInt()

            val id = call.parameters["id"]?.toUIntOrNull() ?: throw IllegalArgumentException("Invalid endpoint ID")
            service.delete(userId, id)

            call.respond(HttpStatusCode.OK)
        }
    }
}