package pt.isel.api_pm.dto.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class CheckRequest(
    val url: String
)