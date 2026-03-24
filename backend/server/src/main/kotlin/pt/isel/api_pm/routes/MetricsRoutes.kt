package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.dto.endpoint.CheckRequest
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService

fun Route.metricsRoutes(
    metricsService: MetricsService,
    monitoringService: MonitoringService,
) {
    route("/api/metrics") {
        authenticate("auth-jwt") {
            // to be removed in the future
            get {
                call.respond(metricsService.getAll())
            }

            // "Manually" check an endpoint, maybe to be removed in the future
            post("/check") {
                val request = call.receive<CheckRequest>()

                val metric = monitoringService.checkEndpoint(request.url)
                // metricsService.save(metric) should only save on the MonitoringWorker this is just a "manual" check

                call.respond(metric)
            }

            get("/{endpoint}") {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim("userId", Int::class) ?: throw InvalidTokenException()

                val endpointId = call.parameters["endpoint"]!!.toInt()
                call.respond(metricsService.getByEndpoint(tokenUserId, endpointId))
            }

            get("/{endpointId}/summary") {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val userId = principal.getClaim("userId", Int::class) ?: throw InvalidTokenException()

                val endpointId = call.parameters["endpointId"]!!.toInt()

                val summary = metricsService.getSummary(userId, endpointId)

                call.respond(summary)
            }
        }
    }
}
