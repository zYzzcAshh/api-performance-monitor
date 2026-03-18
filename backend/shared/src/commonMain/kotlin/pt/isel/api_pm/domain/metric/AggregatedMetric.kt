package pt.isel.api_pm.domain.metric

import pt.isel.api_pm.domain.endpoint.URL
import kotlin.time.Instant

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