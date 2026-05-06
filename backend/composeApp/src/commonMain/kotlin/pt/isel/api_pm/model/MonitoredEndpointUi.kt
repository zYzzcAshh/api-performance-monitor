package pt.isel.api_pm.model

import kotlinx.serialization.Serializable

@Serializable
data class MonitoredEndpointUi(
    val id: UInt,
    val userId: UInt,
    val name: String,
    val url: String,
    val intervalSeconds: Long,
    val createdAt: String,
)