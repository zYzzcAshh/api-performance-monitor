package pt.isel.api_pm.dto.metric

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class AgentRequestMetric(
    val endpointName: String,
    val timestamp: Instant,
    override val latency: Long,
    val statusCode: Int
) : LatencyMetric