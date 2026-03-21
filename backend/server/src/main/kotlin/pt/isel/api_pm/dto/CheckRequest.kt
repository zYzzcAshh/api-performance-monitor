package pt.isel.api_pm.dto

import kotlinx.serialization.Serializable

@Serializable
data class CheckRequest(
    val url: String
)