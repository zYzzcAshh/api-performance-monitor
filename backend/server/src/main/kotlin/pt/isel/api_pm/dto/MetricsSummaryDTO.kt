package pt.isel.api_pm.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetricsSummaryDTO(
    val uptime: Double,
    val averageLatency: Double,
    val totalRequests: Int,
)
