package pt.isel.api_pm.database

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.database.tables.UserTable

object DatabaseInitializer {
    fun connect(config: DatabaseConfig): Database {
        return Database.connect(
            url = config.jdbcUrl,
            driver = config.driver,
            user = config.username,
            password = config.password
        )
    }

    fun init(db: Database) {
        transaction(db) {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(MonitoredEndpointTable)
            SchemaUtils.create(RequestMetricsTable)
            // TODO: Missing Agent table
        }
    }
}