package pt.isel.api_pm.domain.metrics

import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.time.Instant

data class EndpointMetrics (
    val endpoint: EndpointUrl,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int
)

fun EndpointMetrics.toRequestMetric() = RequestMetric(
    endpoint = endpoint,
    timestamp = timestamp,
    latency = latency,
    statusCode = statusCode
)

fun List<EndpointMetrics>.toRequestMetrics() = map { it.toRequestMetric() }