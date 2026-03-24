package pt.isel.api_pm.app

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory
import pt.isel.api_pm.SERVER_PORT
import pt.isel.api_pm.app.module.monitoringModule
import pt.isel.api_pm.configure.configureAll
import pt.isel.api_pm.routes.authRoutes
import pt.isel.api_pm.routes.endpointRoutes
import pt.isel.api_pm.routes.metricsRoutes
import pt.isel.api_pm.routes.testRoutes
import pt.isel.api_pm.routes.userRoutes

val ktorClient = HttpClient(CIO)

private val logger = LoggerFactory.getLogger(Application::class.java)

private suspend fun defaultExternalRequest(): Pair<Int, String> {
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
    logger.info("Starting Application...")

    configureAll()

    val dependencies = AppDependencies()

    monitoringModule(
        dependencies.monitoringService,
        dependencies.metricsService,
        dependencies.endpointService,
    )

    routing {
        userRoutes(dependencies.userService)
        authRoutes(dependencies.authService)
        metricsRoutes(
            dependencies.metricsService,
            dependencies.monitoringService,
        )
        endpointRoutes(dependencies.endpointService)
        testRoutes(externalRequest)
    }
}
