package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.toAgentMessageMetricsList
import pt.isel.api_pm.domain.metrics.toRequestMetrics
import pt.isel.api_pm.dto.endpoint.CheckRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.utils.requireUIntParameter
import pt.isel.api_pm.utils.requireUserId

fun Route.metricsRoutes(
    metricsService: MetricsService,
) {
    get("/metrics/agent") {
        // TODO: Just for testing, needs to be removed in the future
        call.respond(metricsService.getAllAgentMetrics().toAgentMessageMetricsList())
    }

    authenticate(AuthConfig.JWT_NAME) {

        get(Routes.Metrics.BASE) {
            call.respond(
                metricsService.getAll().toRequestMetrics()
            )
        }

        get(Routes.Metrics.BY_ENDPOINT) {

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val endpointId =
                call.parameters["endpoint"]
                    .requireUIntParameter("endpoint ID")

            val message = metricsService.getByEndpoint(
                userId,
                endpointId
            ).toRequestMetrics()

            call.respond(
                message
            )
        }

        get(Routes.Metrics.SUMMARY) {

            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val endpointId =
                call.parameters["endpointId"]
                    .requireUIntParameter("endpoint ID")

            val summary =
                metricsService.getSummary(
                    userId,
                    endpointId
                )

            call.respond(summary)
        }
    }
}