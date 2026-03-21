package pt.isel.api_pm.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateEndpointRequest(
    val url: String,
    val name: String,
    val intervalSeconds: Long
)