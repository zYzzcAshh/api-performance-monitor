package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import pt.isel.api_pm.domain.user.LoginRequest
import pt.isel.api_pm.domain.user.RegisterRequest
import pt.isel.api_pm.service.AuthService

fun Route.authRoutes(service: AuthService) {
    route("/api/auth") {
        post("/register") {
            val request = call.receive<RegisterRequest>()
            service.register(request.username, request.password)
            call.respond(HttpStatusCode.Created, "User registered successfully")
        }

        post("/login") {
            val request = call.receive<LoginRequest>()
            val token = service.login(request.username, request.password)
            call.respond(HttpStatusCode.OK, "User logged in, token: $token")
        }
    }
}
