package pt.isel.api_pm.domain.metrics

import pt.isel.api_pm.dto.message.AgentMessage
import kotlin.time.Instant

data class AgentEndpointMetrics (
    val endpointName: String,
    val statusCode: Int,
    val latency: Long,
    val timestamp: Instant,
)

fun AgentMessage.Metrics.toAgentEndpointMetrics() =
    AgentEndpointMetrics(
        endpointName,
        statusCode,
        latency,
        timestamp
    )

fun AgentEndpointMetrics.toAgentMessageMetrics() = AgentMessage.Metrics(
    endpointName,
    statusCode,
    latency,
    timestamp
)

fun List<AgentEndpointMetrics>.toAgentMessageMetricsList() = map { it.toAgentMessageMetrics() }