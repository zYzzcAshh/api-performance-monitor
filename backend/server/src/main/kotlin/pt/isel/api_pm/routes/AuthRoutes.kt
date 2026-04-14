package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.RegisterRequest
import pt.isel.api_pm.service.AuthService

fun Route.authRoutes(service: AuthService) {

    post(Routes.Auth.REGISTER) {
        val request = call.receive<RegisterRequest>()

        val username = Username(request.username)
        val password = Password(request.password)

        service.register(username, password)

        call.respond(HttpStatusCode.Created)
    }

    post(Routes.Auth.LOGIN) {
        val request = call.receive<LoginRequest>()

        val username = Username(request.username)
        val password = Password(request.password)

        val token = service.login(username, password)

        call.respond(token)
    }
}
