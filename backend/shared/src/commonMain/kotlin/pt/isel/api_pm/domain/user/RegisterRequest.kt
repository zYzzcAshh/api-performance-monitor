package pt.isel.api_pm.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest (
    val username: String,
    val password: String
)