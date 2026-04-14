package pt.isel.api_pm.routes

import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import kotlinx.coroutines.delay
import pt.isel.api_pm.app.ktorClient

private suspend fun defaultExternalRequest(): Pair<Int, String> {
    val response =
        ktorClient.get("https://api.github.com/") {
            header("User-Agent", "Ktor-App")
        }
    return response.status.value to response.bodyAsText()
}

fun Route.testRoutes() {

    get(Routes.Test.GITHUB) {
        val (status, body) = defaultExternalRequest()
        call.respondText(body, ContentType.Application.Json, HttpStatusCode.fromValue(status))
    }

    get(Routes.Test.OK) {
        call.respond(HttpStatusCode.OK, "OK")
    }

    get(Routes.Test.ERROR) {
        call.respond(HttpStatusCode.InternalServerError, "Internal Server Error")
    }

    get(Routes.Test.NOT_FOUND) {
        call.respond(HttpStatusCode.NotFound, "Resource Not Found")
    }

    get(Routes.Test.SLOW) {
        delay(3000)
        call.respond(HttpStatusCode.OK, "Slow response")
    }

    get(Routes.Test.RANDOM) {
        val statuses = listOf(HttpStatusCode.OK, HttpStatusCode.BadRequest, HttpStatusCode.ServiceUnavailable)
        val status = statuses.random()
        val delayMs = (500..2000).random().toLong()
        delay(delayMs)
        call.respond(status, "Random response after $delayMs ms")
    }
}
