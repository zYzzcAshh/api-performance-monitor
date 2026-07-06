package pt.isel.api_pm.service

import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.metric.AggregatedMetric
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds

class MetricsService(
    private val repo: MetricsRepository,
) {
    suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: EndpointMetrics,
    ) = repo.save(userId, monitoredEndpointId, metric)

    suspend fun getAll(): List<EndpointMetrics> = repo.getAll()

    suspend fun saveAgentMetrics(userId: UInt, agentId: UInt, message: AgentEndpointMetrics) = repo.saveAgentMetrics(userId, agentId, message)

    suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<EndpointMetrics> = repo.getByEndpoint(userId, monitoredEndpointId)

    suspend fun getByAgent(
        userId: UInt,
        agentId: UInt,
    ): List<AgentEndpointMetrics> = repo.getByAgent(userId, agentId)

    suspend fun getSummary(
        userId: UInt,
        endpointId: UInt,
    ): AggregatedMetric {

        val metrics =
            getByEndpoint(
                userId,
                endpointId
            )

        if (metrics.isEmpty()) {

            return AggregatedMetric(
                endpoint = EndpointUrl("https://unknown.com"),
                startTime = Clock.System.now(),
                endTime = Clock.System.now(),
                averageLatency = 0.0,
                totalRequests = 0,
                errorRate = 0.0,
                throughput = 0,
                uptime = 0.0,
                percentile95 = 0,
                percentile99 = 0,
                statusCodeDistribution = emptyMap()
            )
        }

        val endpoint =
            metrics.first().endpoint

        val startTime =
            metrics.minBy { it.timestamp }
                .timestamp

        val endTime =
            metrics.maxBy { it.timestamp }
                .timestamp

        val totalRequests =
            metrics.size.toLong()

        val successCount =
            metrics.count {
                it.statusCode in 200..299
            }

        val uptime =
            ((successCount.toDouble() / totalRequests) * 100)

        val averageLatency =
            metrics.map { it.latency }
                .average()

        val errorCount =
            metrics.count {
                it.statusCode >= 400
            }

        val errorRate =
            (errorCount.toDouble() / totalRequests) * 100

        val latencies =
            metrics.map { it.latency }
                .sorted()

        val percentile95 =
            latencies[
                ((latencies.size - 1) * 0.95).toInt()
            ]

        val percentile99 =
            latencies[
                ((latencies.size - 1) * 0.99).toInt()
            ]

        val statusCodeDistribution =
            metrics.groupingBy {
                it.statusCode
            }
                .eachCount()
                .mapValues {
                    it.value.toLong()
                }

        val durationSeconds =
            (endTime - startTime)
                .inWholeSeconds
                .coerceAtLeast(1)

        val throughput =
            totalRequests / durationSeconds

        return AggregatedMetric(
            endpoint = endpoint,
            startTime = startTime,
            endTime = endTime,
            averageLatency = averageLatency,
            totalRequests = totalRequests,
            errorRate = errorRate,
            throughput = throughput,
            uptime = uptime,
            percentile95 = percentile95,
            percentile99 = percentile99,
            statusCodeDistribution = statusCodeDistribution
        )
    }

    suspend fun getMetricsHistoryByAlert(
        userId: UInt,
        endpointId: UInt,
        alertRule: AlertRule
    ): List<EndpointMetrics> {
        val now = Clock.System.now()

        val from = now.minus(alertRule.durationSeconds.value.seconds)

        return repo.getByInterval(
            userId = userId,
            monitoredEndpointId = endpointId,
            from = from,
            to = now
        )
    }

    suspend fun getAllAgentMetrics(): List<AgentEndpointMetrics> = repo.getAllAgentMetrics()
}
