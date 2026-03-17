package com.apimonitor.domain.metric

import java.net.URI
import java.time.Instant

data class AggregatedMetric (
    val endpoint: URI,
    val startTime: Instant,
    val endTime: Instant,
    val averageLatency: Double,
    val totalRequests: Long,
    val errorRate: Double,
    val throughput: Long,
    val uptime: Long,
    val percentile95: Long,
    val percentile99: Long,
    val statusCodeDistribution: Map<Int, Long>
)

/*
    Example:

    // ... after collecting individual RequestMetric instances for a specific endpoint and time window ...
    val aggregatedMetric = AggregatedMetric(
        endpoint = URI("https://api.example.com/data"),
        startTime = Instant.parse("2024-01-01T00:00:00Z"),
        endTime = Instant.parse("2024-01-01T01:00:00Z"),
        averageLatency = 150.0,
        totalRequests = 1000,
        errorRate = 0.05,
        throughput = 16,
        uptime = 3600,
        percentile95 = 300,
        percentile99 = 500,
        statusCodeDistribution = mapOf(200 to 950, 500 to 50)
    )
 */