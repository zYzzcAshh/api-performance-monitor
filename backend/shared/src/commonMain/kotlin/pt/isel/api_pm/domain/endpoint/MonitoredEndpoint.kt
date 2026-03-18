package pt.isel.api_pm.domain.endpoint

import kotlin.time.Instant

data class MonitoredEndpoint (
    val id: Int,
    val userId: Int,
    val url: URL,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: Instant
)

typealias URL = String