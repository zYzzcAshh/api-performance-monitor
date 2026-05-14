package pt.isel.api_pm.dto.user

import kotlinx.serialization.Serializable

@Serializable
data class RegisterResponse(
    val userId: UInt
)

@Serializable
data class LoginResponse(
    val token: String
)