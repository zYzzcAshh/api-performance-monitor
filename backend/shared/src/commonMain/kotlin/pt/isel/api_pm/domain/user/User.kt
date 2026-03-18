package pt.isel.api_pm.domain.user

import kotlin.time.Instant

data class User (
    val id: Int,
    val username: String,
    val passwordHash: String,
    val createdAt: Instant,
)