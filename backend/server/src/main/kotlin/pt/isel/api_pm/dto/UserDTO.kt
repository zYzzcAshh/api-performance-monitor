package pt.isel.api_pm.dto

import kotlinx.serialization.Serializable
import pt.isel.api_pm.domain.user.User
import kotlin.time.Instant

@Serializable
data class UserDTO(
    val id: Int,
    val username: String,
    val createdAt: Instant,
)

fun User.toDTO(): UserDTO =
    UserDTO(
        id = id.value,
        username = username.value,
        createdAt = createdAt,
    )
