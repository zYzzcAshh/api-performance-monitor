package pt.isel.api_pm.configure

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Post)

        allowHost("localhost:8080")
        allowHost("localhost:8081")
        allowCredentials = true

        allowHeader(HttpHeaders.ContentType)
    }
}
