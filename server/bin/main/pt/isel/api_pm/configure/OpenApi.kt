package pt.isel.api_pm.configure

import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.plugins.openapi.*

fun Application.configureOpenApi() {
    routing {
        openAPI(path = "openapi", swaggerFile = "openapi.yaml")

        swaggerUI(
            path = "swagger",
            swaggerFile = "openapi.yaml"
        )
    }
}