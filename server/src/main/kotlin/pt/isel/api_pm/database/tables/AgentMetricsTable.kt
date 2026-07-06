package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object AgentMetricsTable : Table("agent_metrics") {
    val id = long("id").autoIncrement()

    val userId = integer("user_id").references(UserTable.id)
    val agentId = integer("endpoint_id").references(AgentTable.id)

    val endpointName = varchar("endpoint_name", 255)

    val statusCode = integer("status_code")
    val latency = long("latency")

    val timestamp = timestamp("timestamp")

    override val primaryKey = PrimaryKey(id)
}