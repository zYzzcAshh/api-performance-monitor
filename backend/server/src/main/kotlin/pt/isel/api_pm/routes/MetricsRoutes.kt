package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.endpoint.CheckRequest
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService

fun Route.metricsRoutes(
    metricsService: MetricsService,
    monitoringService: MonitoringService,
) {
    route(Routes.Metrics.BASE) {
        authenticate(AuthConfig.JWT_NAME) {
            get {
                call.respond(metricsService.getAll())
            }

            post(Routes.Metrics.CHECK) {
                val request = call.receive<CheckRequest>()

                val url = EndpointUrl(request.url)

                val metric = monitoringService.checkEndpoint(url)

                call.respond(metric)
            }

            get(Routes.Metrics.BY_ENDPOINT) {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
                val userId = tokenUserId.toUInt()

                val endpointId = call.parameters["endpoint"]!!.toUInt()
                call.respond(metricsService.getByEndpoint(userId, endpointId))
            }

            get(Routes.Metrics.SUMMARY) {
                val principal = call.principal<JWTPrincipal>() ?: throw MissingTokenException()
                val tokenUserId = principal.getClaim(AuthConfig.USER_ID_CLAIM, Int::class) ?: throw InvalidTokenException()
                val userId = tokenUserId.toUInt()

                val endpointId = call.parameters["endpointId"]!!.toUInt()

                val summary = metricsService.getSummary(userId, endpointId)

                call.respond(summary)
            }
        }
    }
}
