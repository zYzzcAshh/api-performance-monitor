package pt.isel.api_pm.configure

import io.ktor.server.application.Application

fun Application.configureAll() {
    configureContentNegotiation()
    configureStatusPages()
    configureAuthentication()
}
