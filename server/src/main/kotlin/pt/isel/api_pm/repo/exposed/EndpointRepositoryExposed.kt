package pt.isel.api_pm.repo.exposed

import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.domain.endpoint.EndpointUrl
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.domain.endpoint.MonitoredEndpoint
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.EndpointRepository
import kotlin.time.Clock
import kotlin.time.Instant

class EndpointRepositoryExposed(
    private val db: Database
) : EndpointRepository {
    override suspend fun getAll(): List<MonitoredEndpoint> = transaction(db) {
        MonitoredEndpointTable.selectAll().map {
            it.toEndpoint()
        }
    }

    override suspend fun getByUser(userId: UInt): List<MonitoredEndpoint> = transaction(db) {
        MonitoredEndpointTable.selectAll()
            .where { MonitoredEndpointTable.userId eq userId.toInt() }
            .map { it.toEndpoint() }
    }

    override suspend fun add(
        userId: UInt,
        url: String,
        name: String,
        intervalSeconds: Long,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {
        transaction(db) {
            val (notifType, notifData) = notification.toDb()
            val (alertType, alertData) = alertRule?.toDb() ?: (null to null)

            MonitoredEndpointTable.insert {
                it[MonitoredEndpointTable.userId] = userId.toInt()
                it[MonitoredEndpointTable.url] = url.removeSuffix("/")
                it[MonitoredEndpointTable.name] = name
                it[MonitoredEndpointTable.intervalSeconds] = intervalSeconds
                it[MonitoredEndpointTable.createdAt] = Clock.System.now()

                it[MonitoredEndpointTable.notificationType] = notifType
                it[MonitoredEndpointTable.notificationData] = notifData

                it[MonitoredEndpointTable.alertRuleType] = alertType
                it[MonitoredEndpointTable.alertRuleData] = alertData
            }
        }
    }

    override suspend fun delete(
        userId: UInt,
        monitoredEndpointId: UInt,
    ) {
        transaction(db) {
            MonitoredEndpointTable.deleteWhere {
                (MonitoredEndpointTable.userId eq userId.toInt()) and (MonitoredEndpointTable.id eq monitoredEndpointId.toInt())
            }
        }
    }

    override suspend fun existsByUrlAndUser(
        userId: UInt,
        url: String,
    ): Boolean = transaction(db) {
        MonitoredEndpointTable
            .selectAll()
            .where {
                (MonitoredEndpointTable.userId eq userId.toInt()) and (MonitoredEndpointTable.url eq url.removeSuffix("/"))
            }
            .any()
    }

    // TODO: Helper functions below need to be organized better

    private fun ResultRow.toEndpoint(): MonitoredEndpoint =
        MonitoredEndpoint(
            id = this[MonitoredEndpointTable.id].toUInt(),
            userId = this[MonitoredEndpointTable.userId].toUInt(),
            url = EndpointUrl(this[MonitoredEndpointTable.url]),
            name = this[MonitoredEndpointTable.name],
            interval = IntervalSeconds(this[MonitoredEndpointTable.intervalSeconds]),
            createdAt = this[MonitoredEndpointTable.createdAt],
            notification = this.toNotification(),
            alertRule = this.toAlertRule()
        )

    private fun ResultRow.toNotification(): NotificationConfig {
        val type = this[MonitoredEndpointTable.notificationType]
        val data = this[MonitoredEndpointTable.notificationData]

        return when (type) {
            "none" -> NotificationConfig.None
            "log" -> NotificationConfig.Log
            "discord_webhook" ->
                json.decodeFromString<NotificationConfig.DiscordWebhook>(data!!)
            "email" ->
                json.decodeFromString<NotificationConfig.Email>(data!!)
            "slack_webhook" ->
                json.decodeFromString<NotificationConfig.SlackWebhook>(data!!)
            else -> NotificationConfig.None
        }
    }

    private fun ResultRow.toAlertRule(): AlertRule? {
        val type = this[MonitoredEndpointTable.alertRuleType] ?: return null
        val data = this[MonitoredEndpointTable.alertRuleData] ?: return null

        return when (type) {
            "status_code" ->
                json.decodeFromString<AlertRule.StatusCodeRule>(data)

            "latency" ->
                json.decodeFromString<AlertRule.LatencyRule>(data)

            "down_time" ->
                json.decodeFromString<AlertRule.DownTimeRule>(data)

            else -> null
        }
    }

    fun NotificationConfig.toDb(): Pair<String, String?> {
        return when (this) {
            NotificationConfig.None -> "none" to null
            NotificationConfig.Log -> "log" to null

            is NotificationConfig.DiscordWebhook ->
                "discord_webhook" to json.encodeToString(this)

            is NotificationConfig.Email ->
                "email" to json.encodeToString(this)

            is NotificationConfig.SlackWebhook ->
                "slack_webhook" to json.encodeToString(this)
        }
    }

    fun AlertRule.toDb(): Pair<String, String> {
        return when (this) {
            is AlertRule.StatusCodeRule ->
                "status_code" to json.encodeToString(this)

            is AlertRule.LatencyRule ->
                "latency" to json.encodeToString(this)

            is AlertRule.DownTimeRule ->
                "down_time" to json.encodeToString(this)
        }
    }

    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
        }
    }
}
