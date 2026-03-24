package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable
import kotlin.time.Instant

@Serializable
data class MonitoredEndpoint (
    val id: Int,
    val userId: Int,
    val url: URL,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: Instant
)

typealias URL = String