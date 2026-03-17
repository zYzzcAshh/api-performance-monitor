package com.apimonitor.domain.endpoint

import java.net.URI
import java.time.Instant
import java.util.UUID

data class MonitoredEndpoint (
    val id: UUID,
    val userId: UUID,
    val url: URI,
    val name: String,
    val intervalSeconds: Long,
    val createdAt: Instant
)

/*
    Example:

    val endpoint = MonitoredEndpoint(
        id = UUID.randomUUID(),
        userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
        url = URI("https://api.example.com/data"),
        name = "Example API Data Endpoint",
        intervalSeconds = 60 * 3,
        createdAt = Instant.now()
    )

    // ...

    val endpoints = HashMap<UUID, MutableList<MonitoredEndpoint>>()
    val users = HashMap<UUID, User>()

    // Find user endpoints by id
    val userId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000")
    val userEndpoints = endpoints[userId] ?: emptyList()
    val user = users[userId] ?: throw IllegalArgumentException("User not found")
 */