package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import pt.isel.api_pm.alert.AlertRule
import pt.isel.api_pm.database.tables.AgentTable
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.domain.agent.Agent
import pt.isel.api_pm.domain.agent.AgentEndpoint
import pt.isel.api_pm.domain.endpoint.HttpMethod
import pt.isel.api_pm.domain.endpoint.IntervalSeconds
import pt.isel.api_pm.notification.NotificationConfig
import pt.isel.api_pm.repo.AgentRepository
import pt.isel.api_pm.repo.exposed.mappers.json
import pt.isel.api_pm.repo.exposed.serializers.toDb
import kotlin.time.Clock

class AgentRepositoryExposed(
    private val db: Database
) : AgentRepository {

    override suspend fun register(
        userId: UInt,
        name: String
    ): Agent {

        val createdAt =
            Clock.System.now()

        val id =
            transaction(db) {

                AgentTable.insert {
                    it[AgentTable.userId] = userId.toInt()
                    it[AgentTable.name] = name
                    it[AgentTable.createdAt] = createdAt
                    it[AgentTable.active] = false
                    it[AgentTable.endpointNotificationType] = "None"
                }[AgentTable.id]
            }

        return Agent(
            id = id.toUInt(),
            userId = userId,
            name = name,
            createdAt = createdAt,
            endpoint = null,
            active = false
        )
    }

    override suspend fun addEndpoint(
        userId: UInt,
        agentId: UInt,
        name: String,
        method: HttpMethod,
        intervalSeconds: IntervalSeconds,
        notification: NotificationConfig,
        alertRule: AlertRule?
    ) {

        transaction(db) {

            val existing =
                AgentTable
                    .selectAll()
                    .where {
                        (AgentTable.id eq agentId.toInt()) and
                                (AgentTable.userId eq userId.toInt())
                    }
                    .singleOrNull()
                    ?: throw IllegalArgumentException(
                        "Agent with id $agentId not found"
                    )

            if (existing[AgentTable.endpointName] != null) {

                throw IllegalStateException(
                    "Agent with id $agentId already has an endpoint"
                )
            }

            val (notifType, notifData) =
                notification.toDb()

            val (alertType, alertData) =
                alertRule?.toDb() ?: (null to null)

            AgentTable.update({
                (AgentTable.id eq agentId.toInt()) and
                        (AgentTable.userId eq userId.toInt())
            }) {

                it[endpointName] = name
                it[endpointMethod] = method
                it[endpointIntervalSeconds] = intervalSeconds.value
                it[endpointCreatedAt] = Clock.System.now()
                it[MonitoredEndpointTable.notificationType] = notifType
                it[MonitoredEndpointTable.notificationData] = notifData
                it[MonitoredEndpointTable.alertRuleType] = alertType
                it[MonitoredEndpointTable.alertRuleData] = alertData
                it[active] = true
            }
        }
    }

    override suspend fun inactiveAgent(userId: UInt, agentId: UInt) {
        transaction(db) {
            val existing =
                AgentTable
                    .selectAll()
                    .where {
                        (AgentTable.id eq agentId.toInt()) and
                                (AgentTable.userId eq userId.toInt())
                    }
                    .singleOrNull()
                    ?: throw IllegalArgumentException(
                        "Agent with id $agentId not found"
                    )

            AgentTable.update({
                (AgentTable.id eq agentId.toInt()) and
                        (AgentTable.userId eq userId.toInt())
            }) {

                it[active] = false
            }
        }
    }

    override suspend fun activeAgent(userId: UInt, agentId: UInt) {
        transaction(db) {
            val existing =
                AgentTable
                    .selectAll()
                    .where {
                        (AgentTable.id eq agentId.toInt()) and
                                (AgentTable.userId eq userId.toInt())
                    }
                    .singleOrNull()
                    ?: throw IllegalArgumentException(
                        "Agent with id $agentId not found"
                    )

            AgentTable.update({
                (AgentTable.id eq agentId.toInt()) and
                        (AgentTable.userId eq userId.toInt())
            }) {

                it[active] = true
            }
        }
    }

    override suspend fun getById(
        userId: UInt,
        agentId: UInt
    ): Agent? =
        transaction(db) {

            AgentTable
                .selectAll()
                .where {
                    (AgentTable.id eq agentId.toInt()) and
                            (AgentTable.userId eq userId.toInt())
                }
                .singleOrNull()
                ?.toAgent()
        }

    override suspend fun getAll(): List<Agent> = transaction(db) {
        AgentTable.selectAll()
            .map { it.toAgent() }
    }

    override suspend fun getAllByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent> = transaction(db) {
        AgentTable.selectAll()
            .where { AgentTable.endpointIntervalSeconds eq intervalSeconds.value }
            .map { it.toAgent() }
    }

    override suspend fun getAllActiveByIntervalSeconds(intervalSeconds: IntervalSeconds): List<Agent> =
        transaction(db) {
            AgentTable.selectAll()
                .where {
                    (AgentTable.endpointIntervalSeconds eq intervalSeconds.value) and
                            (AgentTable.active eq true)
                }
                .map { it.toAgent() }
        }

    private fun ResultRow.toAgent(): Agent {

        val endpointName =
            this[AgentTable.endpointName]

        val endpointMethod = this[AgentTable.endpointMethod]

        val endpointInterval =
            this[AgentTable.endpointIntervalSeconds]

        val endpointCreatedAt =
            this[AgentTable.endpointCreatedAt]

        val endpoint =
            if (
                endpointName != null &&
                endpointInterval != null &&
                endpointCreatedAt != null &&
                endpointMethod != null
            ) {

                AgentEndpoint(
                    name = endpointName,
                    method = endpointMethod,
                    intervalSeconds = IntervalSeconds(
                        endpointInterval
                    ),
                    notification = this.toNotification(),
                    alertRule = this.toAlertRule(),
                    createdAt = endpointCreatedAt
                )

            } else {
                null
            }

        return Agent(
            id = this[AgentTable.id].toUInt(),
            userId = this[AgentTable.userId].toUInt(),
            name = this[AgentTable.name],
            createdAt = this[AgentTable.createdAt],
            endpoint = endpoint,
            active = this[AgentTable.active]
        )
    }

    private fun ResultRow.toNotification(): NotificationConfig {
        val type = this[AgentTable.endpointNotificationType]
        val data = this[AgentTable.endpointNotificationData]

        return when (type) {
            "none" ->
                NotificationConfig.None

            "log" ->
                NotificationConfig.Log

            "discord_webhook" -> {
                val jsonData = data ?: return NotificationConfig.None
                json.decodeFromString<NotificationConfig.DiscordWebhook>(jsonData)
            }

            "email" -> {
                val jsonData = data ?: return NotificationConfig.None
                json.decodeFromString<NotificationConfig.Email>(jsonData)
            }

            "slack_webhook" -> {
                val jsonData = data ?: return NotificationConfig.None
                json.decodeFromString<NotificationConfig.SlackWebhook>(jsonData)
            }

            "telegram" -> {
                val jsonData = data ?: return NotificationConfig.None
                json.decodeFromString<NotificationConfig.Telegram>(jsonData)
            }

            "webhook" -> {
                val jsonData = data ?: return NotificationConfig.None
                json.decodeFromString<NotificationConfig.Webhook>(jsonData)
            }

            else ->
                NotificationConfig.None
        }
    }

    private fun ResultRow.toAlertRule(): AlertRule? {
        val type = this[AgentTable.endpointAlertRuleType] ?: return null
        val data = this[AgentTable.endpointAlertRuleData] ?: return null

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
}