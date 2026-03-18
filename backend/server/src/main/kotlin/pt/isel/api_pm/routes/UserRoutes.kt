package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.userRoutes() {
    route("/api/users") {
        get { // Get all users
            call.respondText("Get all users endpoint - Not implemented yet", status = HttpStatusCode.NotImplemented)
        }
    }
}