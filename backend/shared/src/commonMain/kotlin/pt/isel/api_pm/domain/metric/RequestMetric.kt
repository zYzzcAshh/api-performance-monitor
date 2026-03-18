package pt.isel.api_pm.domain.metric

import pt.isel.api_pm.domain.endpoint.URL
import kotlin.time.Instant

data class RequestMetric (
    val endpoint: URL,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int
)