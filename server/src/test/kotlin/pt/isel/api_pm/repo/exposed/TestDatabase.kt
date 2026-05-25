package pt.isel.api_pm.repo.exposed

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.AgentTable
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.database.tables.UserTable

object TestDatabase {

    val db =
        Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            driver = "org.h2.Driver"
        )

    fun init() {

        transaction(db) {

            SchemaUtils.drop(
                RequestMetricsTable,
                MonitoredEndpointTable,
                AgentTable,
                UserTable
            )

            SchemaUtils.create(
                UserTable,
                AgentTable,
                MonitoredEndpointTable,
                RequestMetricsTable
            )
        }
    }
}