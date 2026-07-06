package pt.isel.api_pm.routes

import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sse.heartbeat
import io.ktor.server.sse.sse
import io.ktor.sse.ServerSentEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import pt.isel.api_pm.utils.AuthConfig
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.toAgentMessageMetrics
import pt.isel.api_pm.domain.metrics.toAgentMessageMetricsList
import pt.isel.api_pm.domain.metrics.toRequestMetric
import pt.isel.api_pm.domain.metrics.toRequestMetrics
import pt.isel.api_pm.dto.endpoint.CheckRequest
import pt.isel.api_pm.exceptions.MissingTokenException
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.utils.MetricsEventBus
import pt.isel.api_pm.utils.requireUIntParameter
import pt.isel.api_pm.utils.requireUserId
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun Route.metricsRoutes(
    metricsService: MetricsService,
    endpointService: EndpointService,
    agentService: AgentService,
    metricsEventBus: MetricsEventBus
) {
    val logger = LoggerFactory.getLogger("MetricsRoutes")

    get("/metrics/agent") {
        // TODO: Just for testing, needs to be removed in the future
        call.respond(metricsService.getAllAgentMetrics().toAgentMessageMetricsList())
    }

    authenticate(AuthConfig.JWT_NAME) {
        sse("/endpoint-event/{endpointId}") {
            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val endpointId =
                call.parameters["endpointId"]
                    .requireUIntParameter("endpoint ID")


            //TODO: NEEDS TO HAVE A getByIds() in endpointService()
            val endpoint = endpointService.getByUser(userId).firstOrNull { it.id == endpointId } ?: throw NotFoundException("Endpoint not found")


//            val heartbeat = launch {
//                while (isActive) {
//                    delay(15.seconds)
//                    send(ServerSentEvent(comments = "keep-alive"))
//                }
//            }

            try {
                heartbeat {
                    period = 15.seconds
                    event = ServerSentEvent("heartbeat")
                }

                metricsEventBus.subscribeEndpoint(userId, endpointId).collect { metric ->
                    send(
                        ServerSentEvent(
                            data = Json.encodeToString(metric.toRequestMetric()),
                        )
                    )
                }
            } catch (e: Exception) {
                logger.warn("Exception caught in metricsEventBus, client may be disconnected", e)
            }
        }

        sse("/agent-event/{agentId}") {
            val principal =
                call.principal<JWTPrincipal>()
                    ?: throw MissingTokenException()

            val userId =
                principal.requireUserId()

            val agentId =
                call.parameters["agentId"]
                    .requireUIntParameter("agent ID")

            val agent = agentService.getByIds(userId, agentId) ?: throw NotFoundException("Agent not found")

            try {
                heartbeat {
                    period = 15.seconds
                    event = ServerSentEvent("heartbeat")
                }

                metricsEventBus.subscribeAgent(userId, agentId).collect { metric ->
                    send(
                        ServerSentEvent(
                            data = Json.encodeToString(metric.toAgentMessageMetrics())
                        )
                    )
                }
            } catch (e: Exception) {
                logger.warn("Exception caught in agentEventBus, client may be disconnected", e)
            }
        }

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