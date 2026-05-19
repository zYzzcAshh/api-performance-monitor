package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.endpoint.CheckRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.utils.requireUIntParameter
import pt.isel.api_pm.utils.requireUserId

fun Route.metricsRoutes(
    metricsService: MetricsService,
    monitoringService: MonitoringService,
) {

    authenticate(AuthConfig.JWT_NAME) {

        get(Routes.Metrics.BASE) {

            call.respond(
                metricsService.getAll()
            )
        }

        post(Routes.Metrics.CHECK) {

            val request =
                call.receive<CheckRequest>()

            val url =
                EndpointUrl(request.url)

            val metric =
                monitoringService.checkEndpoint(url)

            call.respond(metric)
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

            call.respond(
                metricsService.getByEndpoint(
                    userId,
                    endpointId
                )
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