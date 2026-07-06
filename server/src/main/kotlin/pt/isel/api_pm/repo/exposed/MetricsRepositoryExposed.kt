package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.AgentMetricsTable
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.metrics.AgentEndpointMetrics
import pt.isel.api_pm.domain.metrics.EndpointMetrics
import pt.isel.api_pm.dto.message.AgentMessage
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import kotlin.time.Instant

class MetricsRepositoryExposed(
    private val db: Database,
) : MetricsRepository {
    override suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: EndpointMetrics,
    ) {
        transaction(db) {
            RequestMetricsTable.insert {
                it[RequestMetricsTable.userId] = userId.toInt()
                it[RequestMetricsTable.endpointId] = monitoredEndpointId.toInt()

                it[url] = metric.endpoint.normalized()
                it[timestamp] = metric.timestamp
                it[latency] = metric.latency
                it[statusCode] = metric.statusCode
            }
        }
    }

    override suspend fun getByEndpoint(
        userId: UInt,
        monitoredEndpointId: UInt,
    ): List<EndpointMetrics> =
        transaction(db) {
            RequestMetricsTable
                .selectAll()
                .where {
                    (RequestMetricsTable.userId eq userId.toInt()) and (RequestMetricsTable.endpointId eq monitoredEndpointId.toInt())
                }
                .map { it.toMetric() }
        }

    override suspend fun getByAgent(
        userId: UInt,
        agentId: UInt
    ): List<AgentEndpointMetrics> {
        return transaction(db) {
            AgentMetricsTable
                .selectAll()
                .where {
                    (AgentMetricsTable.userId eq userId.toInt()) and (AgentMetricsTable.agentId eq agentId.toInt())
                }
                .map {
                    AgentEndpointMetrics(
                        endpointName = it[AgentMetricsTable.endpointName],
                        statusCode = it[AgentMetricsTable.statusCode],
                        latency = it[AgentMetricsTable.latency],
                        timestamp = it[AgentMetricsTable.timestamp]
                    )
                }
        }
    }

    override suspend fun getAll(): List<EndpointMetrics> =
        transaction(db) {
            RequestMetricsTable
                .selectAll()
                .map { it.toMetric() }
        }

    override suspend fun getByInterval(
        userId: UInt,
        monitoredEndpointId: UInt,
        from: Instant,
        to: Instant
    ): List<EndpointMetrics> =
    transaction(db) {
        RequestMetricsTable
            .selectAll()
            .where {
                (RequestMetricsTable.userId eq userId.toInt()) and
                        (RequestMetricsTable.endpointId eq monitoredEndpointId.toInt()) and
                        (RequestMetricsTable.timestamp.between(from, to))
            }
            .map { it.toMetric() }
    }

    override suspend fun saveAgentMetrics(
        userId: UInt,
        agentId: UInt,
        message: AgentEndpointMetrics
    ) {
        transaction(db) {
            AgentMetricsTable.insert {
                it[AgentMetricsTable.userId] = userId.toInt()
                it[AgentMetricsTable.agentId] = agentId.toInt()

                it[endpointName] = message.endpointName
                it[statusCode] = message.statusCode
                it[latency] = message.latency
                it[timestamp] = message.timestamp
            }
        }
    }

    override suspend fun getAllAgentMetrics(): List<AgentEndpointMetrics> = transaction(db) {
        AgentMetricsTable.selectAll()
            .map {
                AgentEndpointMetrics(
                    endpointName = it[AgentMetricsTable.endpointName],
                    statusCode = it[AgentMetricsTable.statusCode],
                    latency = it[AgentMetricsTable.latency],
                    timestamp = it[AgentMetricsTable.timestamp]
                )
            }
    }

    override suspend fun getAgentMetricsByInterval(
        userId: UInt,
        agentId: UInt,
        from: Instant,
        to: Instant
    ): List<AgentEndpointMetrics> = transaction(db) {
        AgentMetricsTable.selectAll()
            .where {
                (AgentMetricsTable.userId eq userId.toInt()) and
                        (AgentMetricsTable.agentId eq agentId.toInt()) and
                        (AgentMetricsTable.timestamp.between(from, to))
            }
            .map {
                AgentEndpointMetrics(
                    endpointName = it[AgentMetricsTable.endpointName],
                    statusCode = it[AgentMetricsTable.statusCode],
                    latency = it[AgentMetricsTable.latency],
                    timestamp = it[AgentMetricsTable.timestamp]
                )
            }
    }

    private fun ResultRow.toMetric(): EndpointMetrics =
        EndpointMetrics(
            endpoint = EndpointUrl(this[RequestMetricsTable.url]),
            timestamp = this[RequestMetricsTable.timestamp],
            latency = this[RequestMetricsTable.latency],
            statusCode = this[RequestMetricsTable.statusCode]
        )
}
