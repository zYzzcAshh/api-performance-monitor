package pt.isel.api_pm.domain.metrics

import pt.isel.api_pm.dto.message.AgentMessage

data class AgentEndpointMetrics (
    val endpointName: String,
    val statusCode: Int,
    val responseTimeMs: Long,
    val timestamp: Long,
)

fun AgentMessage.Metrics.toAgentEndpointMetrics() =
    AgentEndpointMetrics(
        endpointName,
        statusCode,
        responseTimeMs,
        timestamp
    )

fun AgentEndpointMetrics.toAgentMessageMetrics() = AgentMessage.Metrics(
    endpointName,
    statusCode,
    responseTimeMs,
    timestamp
)

fun List<AgentEndpointMetrics>.toAgentMessageMetricsList() = map { it.toAgentMessageMetrics() }