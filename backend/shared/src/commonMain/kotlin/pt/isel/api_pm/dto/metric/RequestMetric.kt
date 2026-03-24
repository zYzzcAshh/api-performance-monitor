package pt.isel.api_pm.dto.metric

import kotlinx.serialization.Serializable
import pt.isel.api_pm.dto.endpoint.URL
import kotlin.time.Instant

@Serializable
data class RequestMetric (
    val endpoint: URL,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int
)