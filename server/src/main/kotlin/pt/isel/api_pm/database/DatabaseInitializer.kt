package pt.isel.api_pm.database

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.MonitoredEndpointTable
import pt.isel.api_pm.database.tables.RequestMetricsTable
import pt.isel.api_pm.database.tables.UserTable
import java.io.File

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

    fun createDatabase(useH2: Boolean): DatabaseConfig {
        // TODO: Needs some changes
        return if (useH2) {
            // TODO: Temporary before i find a better way
            File("/testdb.mv.db").delete()
            File("/testdb.trace.db").delete()

            DatabaseConfig(
                jdbcUrl = "jdbc:h2:./testdb;AUTO_SERVER=TRUE",
                username = "sa",
                password = "",
                driver = "org.h2.Driver"
            )
        } else {
            DatabaseConfig(
                jdbcUrl = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/app",
                username = System.getenv("DB_USER") ?: "postgres",
                password = System.getenv("DB_PASSWORD") ?: "password",
                driver = "org.postgresql.Driver"
            )
        }
    }
}