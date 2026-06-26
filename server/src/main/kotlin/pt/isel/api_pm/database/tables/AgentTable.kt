package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.ReferenceOption
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

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

    val endpointIntervalSeconds =
        long("endpoint_interval_seconds")
            .nullable()

    val endpointCreatedAt =
        timestamp("endpoint_created_at")
            .nullable()

    override val primaryKey =
        PrimaryKey(id)
}