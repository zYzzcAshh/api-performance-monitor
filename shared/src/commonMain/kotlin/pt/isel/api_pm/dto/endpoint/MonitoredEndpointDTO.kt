package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint

@Serializable
data class MonitoredEndpointDTO(
    val id: UInt,
    val userId: UInt,
    val url: String,
    val name: String,
    val method: HttpMethod,
    val intervalSeconds: Long,
    val createdAt: String,
    val active: Boolean
)

fun MonitoredEndpoint.toDTO() = MonitoredEndpointDTO(
    id = id,
    userId = userId,
    url = url.value,
    name = name,
    method = method,
    intervalSeconds = interval.value,
    createdAt = createdAt.toString(),
    active = active
)

fun List<MonitoredEndpoint>.toDTO() = map { it.toDTO() }