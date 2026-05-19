package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.between
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.dto.metric.RequestMetric
import pt.isel.api_pm.repo.MetricsRepository
import kotlin.time.Instant

class MetricsRepositoryExposed(
    private val db: Database,
) : MetricsRepository {
    override suspend fun save(
        userId: UInt,
        monitoredEndpointId: UInt,
        metric: RequestMetric,
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
    ): List<RequestMetric> =
        transaction(db) {
            RequestMetricsTable
                .selectAll()
                .where {
                    (RequestMetricsTable.userId eq userId.toInt()) and (RequestMetricsTable.endpointId eq monitoredEndpointId.toInt())
                }
                .map { it.toMetric() }
        }

    override suspend fun getAll(): List<RequestMetric> =
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
    ): List<RequestMetric> =
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

    private fun ResultRow.toMetric(): RequestMetric =
        RequestMetric(
            endpoint = EndpointUrl(this[RequestMetricsTable.url]),
            timestamp = this[RequestMetricsTable.timestamp],
            latency = this[RequestMetricsTable.latency],
            statusCode = this[RequestMetricsTable.statusCode]
        )
}
