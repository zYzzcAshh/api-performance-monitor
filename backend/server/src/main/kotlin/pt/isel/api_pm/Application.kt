package pt.isel.api_pm

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.routes.authRoutes
import pt.isel.api_pm.routes.userRoutes

val ktorClient = HttpClient(CIO)

suspend fun defaultExternalRequest(): Pair<Int, String> {
    val response =
        ktorClient.get("https://api.github.com/") {
            header("User-Agent", "Ktor-App")
        }
    return response.status.value to response.bodyAsText()
}

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(externalRequest: suspend () -> Pair<Int, String> = ::defaultExternalRequest) {
    configureContentNegotiation()
    configureStatusPages()

    val dependencies = AppDependencies(useMemory = true)

    routing {
        userRoutes(dependencies.userService)
        authRoutes(dependencies.authService)

        get("/") {
            call.respondText("Ktor: ${Greeting().greet()}")
        }

        get("api/test") {
            try {
                val (status, body) = externalRequest()
                call.respondText(body, ContentType.Application.Json, HttpStatusCode.fromValue(status))
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}")
            }
        }
    }
}
