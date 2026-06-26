package pt.isel.api_pm.dto.metric

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import kotlin.time.Instant

@Serializable
data class RequestMetric (
    val endpoint: EndpointUrl,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int
)

@Serializable
data class AgentRequestMetric (
    val name: String,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int,
)