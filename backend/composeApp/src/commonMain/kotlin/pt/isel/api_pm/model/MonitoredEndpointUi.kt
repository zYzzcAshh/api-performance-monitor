package pt.isel.api_pm.model

import kotlinx.serialization.Serializable

@Serializable
data class MonitoredEndpointUi(
    val id: UInt,
    val userId: UInt,
    val url: String,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: String
)