package com.apimonitor.domain.user

import java.time.Instant
import java.util.UUID

data class User (
    val id: UUID,
    val username: String,
    val passwordHash: String,
    val createdAt: Instant
)