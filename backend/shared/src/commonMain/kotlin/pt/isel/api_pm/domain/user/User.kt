package pt.isel.api_pm.domain.user

import kotlin.time.Instant

data class User (
    val id: UInt,
    val username: Username,
    val passwordHash: PasswordHash,
    val createdAt: Instant,
)