package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.EndpointRepository
import pt.isel.api_pm.repo.exposed.mappers.toEndpoint
import pt.isel.api_pm.repo.exposed.serializers.toDb
import kotlin.time.Clock

class EndpointRepositoryExposed(
    private val db: Database
) : EndpointRepository {

    override suspend fun getAll(): List<MonitoredEndpoint> =
        transaction(db) {

            MonitoredEndpointTable
                .selectAll()
                .map {
                    it.toEndpoint()
                }
        }

    override suspend fun getByUser(userId: UInt): List<MonitoredEndpoint> =
        transaction(db) {

            MonitoredEndpointTable
                .selectAll()
                .where {
                    MonitoredEndpointTable.userId eq userId.toInt()
                }
                .map {
                    it.toEndpoint()
                }
        }

    override suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        method: HttpMethod,
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {

        transaction(db) {

            val normalizedUrl =
                url.removeSuffix("/")

            val (notifType, notifData) =
                notification.toDb()

            val (alertType, alertData) =
                alertRule?.toDb() ?: (null to null)

            MonitoredEndpointTable.insert {

                it[MonitoredEndpointTable.userId] =
                    userId.toInt()

                it[MonitoredEndpointTable.url] =
                    normalizedUrl

                it[MonitoredEndpointTable.name] =
                    name

                it[MonitoredEndpointTable.method] = method

                it[MonitoredEndpointTable.intervalSeconds] =
                    intervalSeconds

                it[MonitoredEndpointTable.createdAt] =
                    Clock.System.now()

                it[MonitoredEndpointTable.notificationType] =
                    notifType

                it[MonitoredEndpointTable.notificationData] =
                    notifData

                it[MonitoredEndpointTable.alertRuleType] =
                    alertType

                it[MonitoredEndpointTable.alertRuleData] =
                    alertData
            }
        }
    }

    override suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) {

        transaction(db) {

            RequestMetricsTable.deleteWhere {

                RequestMetricsTable.endpointId eq
                        monitoredEndpointId.toInt()
            }

            MonitoredEndpointTable.deleteWhere {

                (MonitoredEndpointTable.userId eq userId.toInt()) and
                        (MonitoredEndpointTable.id eq monitoredEndpointId.toInt())
            }
        }
    }

    override suspend fun existsByUrlAndUser(
        userId: UInt,
        url: String,
    ): Boolean =
        transaction(db) {

            val normalizedUrl =
                url.removeSuffix("/")

            MonitoredEndpointTable
                .selectAll()
                .where {

                    (MonitoredEndpointTable.userId eq userId.toInt()) and
                            (MonitoredEndpointTable.url eq normalizedUrl)
                }
                .any()
        }
}