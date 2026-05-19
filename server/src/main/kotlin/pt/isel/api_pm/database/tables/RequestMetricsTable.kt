package pt.isel.api_pm.database.tables

import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.datetime.timestamp

object RequestMetricsTable : Table("request_metrics") {
    val id = long("id").autoIncrement()

    val userId = integer("user_id").references(UserTable.id)
    val endpointId = integer("endpoint_id").references(MonitoredEndpointTable.id)

    val url = varchar("url", 512)

    val timestamp = timestamp("timestamp")

    val latency = long("latency")

    val statusCode = integer("status_code")

    override val primaryKey = PrimaryKey(id)
}