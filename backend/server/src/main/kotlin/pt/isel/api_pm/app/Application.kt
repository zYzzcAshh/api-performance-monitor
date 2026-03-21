package pt.isel.api_pm.app

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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.slf4j.LoggerFactory
import pt.isel.api_pm.Greeting
import pt.isel.api_pm.SERVER_PORT
import pt.isel.api_pm.configure.configureAll
import pt.isel.api_pm.routes.authRoutes
import pt.isel.api_pm.routes.endpointRoutes
import pt.isel.api_pm.routes.metricsRoutes
import pt.isel.api_pm.routes.userRoutes
import pt.isel.api_pm.worker.MonitoringWorker

val ktorClient = HttpClient(CIO)

private val logger = LoggerFactory.getLogger("Application")

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
    logger.info("Starting application...")

    configureAll()

    val dependencies = AppDependencies(useMemory = true)

    val worker =
        MonitoringWorker(
            dependencies.monitoringService,
            dependencies.metricsService,
            dependencies.endpointService,
        )

    worker.start(CoroutineScope(Dispatchers.Default))

    routing {
        userRoutes(dependencies.userService)
        authRoutes(dependencies.authService)
        metricsRoutes(
            dependencies.metricsService,
            dependencies.monitoringService,
        )
        endpointRoutes(dependencies.endpointService)

        get {
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
