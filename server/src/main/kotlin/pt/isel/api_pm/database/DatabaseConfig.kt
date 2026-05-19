package pt.isel.api_pm.database

data class DatabaseConfig(
    val jdbcUrl: String,
    val username: String,
    val password: String,
    val driver: String,
)