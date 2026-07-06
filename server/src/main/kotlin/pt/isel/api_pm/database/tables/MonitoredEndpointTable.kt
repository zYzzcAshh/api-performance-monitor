package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp
import pt.isel.api_pm.domain.endpoint.HttpMethod

object MonitoredEndpointTable : Table("endpoints") {
    val id = integer("id").autoIncrement()
    val userId = integer("user_id").references(UserTable.id)

    val url = varchar("url", 512)
    val name = varchar("name", 255)

    val method = enumerationByName("method", 10, HttpMethod::class)

    val intervalSeconds = long("interval_seconds")
    val createdAt = timestamp("created_at")

    val notificationType = varchar("notification_type", 50)

    val notificationData = text("notification_data").nullable()

    val alertRuleType = varchar("alert_rule_type", 50).nullable()
    val alertRuleData = text("alert_rule_data").nullable()

    val active = bool("active")

    override val primaryKey = PrimaryKey(id)
}