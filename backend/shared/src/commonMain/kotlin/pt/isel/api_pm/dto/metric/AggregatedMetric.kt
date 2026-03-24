package pt.isel.api_pm.dto.metric

import kotlinx.serialization.Serializable
import pt.isel.api_pm.dto.endpoint.URL
import kotlin.time.Instant

@Serializable
data class AggregatedMetric (
    val endpoint: URL,
    val startTime: Instant,
    val endTime: Instant,
    val averageLatency: Double,
    val totalRequests: Long,
    val errorRate: Double,
    val throughput: Long,
    val uptime: Long,
    val percentile95: Long,
    val percentile99: Long,
    val statusCodeDistribution: Map<Int, Long>
)