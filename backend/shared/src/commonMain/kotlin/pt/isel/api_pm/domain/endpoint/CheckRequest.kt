package pt.isel.api_pm.domain.endpoint

import kotlinx.serialization.Serializable

@Serializable
data class CheckRequest(
    val url: String
)