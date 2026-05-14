package pt.isel.api_pm.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val username: String,
    val password: String
)