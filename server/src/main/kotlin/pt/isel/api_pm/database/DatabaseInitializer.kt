package pt.isel.api_pm.database

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import pt.isel.api_pm.database.tables.AgentMetricsTable
import pt.isel.api_pm.database.tables.AgentTable
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
            password = config.password,

        )
    }

    fun init(db: Database) {

        transaction(db) {

            SchemaUtils.create(
                UserTable,
                AgentTable,
                MonitoredEndpointTable,
                RequestMetricsTable,
                AgentMetricsTable
            )
        }
    }

    fun createDatabase(useH2: Boolean): DatabaseConfig {

        return if (useH2) {

            /*
            H2 db in memory doesn't seem to show tables in Intellij Database tab, using db in file for now
            needs a better way, working for now
             */

            try {
                File("/testdb.mv.db").delete()
                File("/testdb.trace.db").delete()
            } catch (_: Exception) { }

            DatabaseConfig(
                jdbcUrl = "jdbc:h2:./testdb;AUTO_SERVER=TRUE",
                username = "sa",
                password = "",
                driver = "org.h2.Driver"
            )

        } else {

            DatabaseConfig(
                jdbcUrl = System.getenv("DB_URL")
                    ?: "jdbc:postgresql://localhost:5432/app",

                username = System.getenv("DB_USER")
                    ?: "postgres",

                password = System.getenv("DB_PASSWORD")
                    ?: "password",

                driver = "org.postgresql.Driver"
            )
        }
    }
}