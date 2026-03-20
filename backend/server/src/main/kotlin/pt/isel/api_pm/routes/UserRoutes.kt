package pt.isel.api_pm.routes

import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import pt.isel.api_pm.dto.toDTO
import pt.isel.api_pm.service.UserService

fun Route.userRoutes(service: UserService) {
    route("/api/users") {
        get {
            call.respond(service.getUsers().map { user -> user.toDTO() })
        }
    }
}
