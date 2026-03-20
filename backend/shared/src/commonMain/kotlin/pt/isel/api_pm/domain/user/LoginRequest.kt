package pt.isel.api_pm.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest (
    val username: String,
    val password: String
)