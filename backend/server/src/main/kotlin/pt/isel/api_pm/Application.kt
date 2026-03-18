package pt.isel.api_pm

import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val httpClient: HttpClient = HttpClient.newHttpClient()

suspend fun defaultExternalRequest(): Pair<Int, String> {
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://api.github.com/"))
        .header("User-Agent", "Ktor-App")
        .GET()
        .build()

    val response = withContext(Dispatchers.IO) {
        httpClient.send(request, HttpResponse.BodyHandlers.ofString())
    }
    return response.statusCode() to response.body()
}

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module(
    externalRequest: suspend () -> Pair<Int, String> = ::defaultExternalRequest,
) {
    routing {
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