package pt.isel.api_pm.routes

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.dto.CheckRequest
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService

fun Route.metricsRoutes(
    metricsService: MetricsService,
    monitoringService: MonitoringService
) {

    route("/api/metrics") {

        get {
            call.respond(metricsService.getAll())
        }

        get("/{endpoint}") {
            val endpoint = call.parameters["endpoint"]!!
            call.respond(metricsService.getByEndpoint(endpoint))
        }

        post("/check") {
            val request = call.receive<CheckRequest>()

            val metric = monitoringService.checkEndpoint(request.url)
            metricsService.save(metric)

            call.respond(metric)
        }
    }
}