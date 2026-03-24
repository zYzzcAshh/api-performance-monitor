package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint

@Serializable
data class MonitoredEndpointDTO(
    val id: Int,
    val userId: Int,
    val url: String,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: String,
)

fun MonitoredEndpoint.toDTO() = MonitoredEndpointDTO(
    id = id,
    userId = userId,
    url = url.value,
    name = name,
    intervalSeconds = interval.value,
    createdAt = createdAt.toString()
)