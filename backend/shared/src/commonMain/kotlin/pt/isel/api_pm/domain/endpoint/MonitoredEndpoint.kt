package pt.isel.api_pm.domain.endpoint

import kotlin.time.Instant

data class MonitoredEndpoint(
    val id: Int,
    val userId: Int,
    val url: EndpointUrl,
    val name: String,
    val interval: IntervalSeconds,
    val createdAt: Instant,
)