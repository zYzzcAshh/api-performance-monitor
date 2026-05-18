package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.LoginResponse
import pt.isel.api_pm.dto.user.RegisterRequest
import pt.isel.api_pm.dto.user.RegisterResponse
import pt.isel.api_pm.service.AuthService
import pt.isel.api_pm.utils.toCredentials

fun Route.authRoutes(
    service: AuthService
) {

    post(Routes.Auth.REGISTER) {

        val request =
            call.receive<RegisterRequest>()

        val (username, password) =
            request.toCredentials()

        val user =
            service.register(
                username,
                password
            )

        call.respond(
            HttpStatusCode.Created,
            RegisterResponse(user.id)
        )
    }

    post(Routes.Auth.LOGIN) {

        val request =
            call.receive<LoginRequest>()

        val (username, password) =
            request.toCredentials()

        val token =
            service.login(
                username,
                password
            )

        call.respond(
            LoginResponse(token)
        )
    }
}