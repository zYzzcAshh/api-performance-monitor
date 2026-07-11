package pt.isel.api_pm.domain.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class EndpointUiModel(
    val id: UInt,
    val userId: UInt,
    val url: String,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: String,
    val active: Boolean
)