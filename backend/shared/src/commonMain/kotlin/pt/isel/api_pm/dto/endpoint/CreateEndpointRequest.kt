package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class CreateEndpointRequest(
    val url: String,
    val name: String,
    val intervalSeconds: Long
)