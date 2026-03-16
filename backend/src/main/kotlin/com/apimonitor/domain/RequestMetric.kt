package com.apimonitor.domain

import java.net.URI
import java.time.Instant

data class RequestMetric (
    val endpoint: URI,
    val timestamp: Instant,
    val latency: Long,
    val statusCode: Int
)

/*
    Example:

    val startTime = Instant.now()
    // ... make the API request ...
    val endTime = Instant.now()
    val latency = Duration.between(startTime, endTime).toMillis()
    val metric = RequestMetric(
        endpoint = URI("https://api.example.com/data"),
        timestamp = startTime,
        latency = latency,
        statusCode = response.statusCodeValue
    )
 */