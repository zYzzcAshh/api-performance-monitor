package pt.isel.api_pm.dto.metric

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import kotlin.time.Instant

@Serializable
data class AggregatedMetric (
    val endpoint: EndpointUrl,
    val startTime: Instant,
    val endTime: Instant,
    val averageLatency: Double,
    val totalRequests: Long,
    val errorRate: Double,
    val throughput: Long,
    val uptime: Double,
    val percentile95: Long,
    val percentile99: Long,
    val statusCodeDistribution: Map<Int, Long>
)
