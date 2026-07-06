package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp
import pt.isel.api_pm.domain.endpoint.HttpMethod

object AgentTable : Table("agents") {

    val id =
        integer("id").autoIncrement()

    val userId =
        integer("user_id")
            .references(
                UserTable.id,
                onDelete = ReferenceOption.CASCADE
            )

    val name =
        varchar("name", 255)

    val createdAt =
        timestamp("created_at")

    val endpointName =
        varchar("endpoint_name", 255)
            .nullable()

    val endpointMethod =
        enumerationByName("endpoint_method", 10, HttpMethod::class).nullable()

    val endpointIntervalSeconds =
        long("endpoint_interval_seconds")
            .nullable()

    val endpointCreatedAt =
        timestamp("endpoint_created_at")
            .nullable()

    val endpointNotificationType = varchar("notification_type", 50)

    val endpointNotificationData = text("notification_data").nullable()

    val endpointAlertRuleType = varchar("alert_rule_type", 50).nullable()
    val endpointAlertRuleData = text("alert_rule_data").nullable()

    val active = bool("active")

    override val primaryKey =
        PrimaryKey(id)
}