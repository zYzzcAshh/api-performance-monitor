package pt.isel.api_pm.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoutes() {
    route("/api/auth") {
        post("/register") {
           call.respondText("User registration endpoint - Not implemented yet", status = HttpStatusCode.NotImplemented)
        }

        post("/login") {
            call.respondText("User login endpoint - Not implemented yet", status = HttpStatusCode.NotImplemented)
        }
    }
}