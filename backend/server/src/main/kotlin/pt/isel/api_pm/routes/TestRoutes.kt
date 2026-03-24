package pt.isel.api_pm.routes

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.testRoutes(externalRequest: suspend () -> Pair<Int, String>) {
    route("/api/test") {
        get {
            val (status, body) = externalRequest()
            call.respondText(body, ContentType.Application.Json, HttpStatusCode.fromValue(status))
        }
    }
}
