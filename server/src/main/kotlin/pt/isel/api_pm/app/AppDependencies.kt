package pt.isel.api_pm.app

import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.repo.memory.AgentRepositoryMemory
import pt.isel.api_pm.repo.memory.EndpointRepositoryMemory
import pt.isel.api_pm.repo.memory.MetricsRepositoryMemory
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import pt.isel.api_pm.repo.exposed.UserRepositoryExposed
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.AuthService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.JwtService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.NotificationService
import pt.isel.api_pm.service.UserService
import pt.isel.api_pm.database.DatabaseConfig
import pt.isel.api_pm.database.DatabaseInitializer
import pt.isel.api_pm.repo.exposed.EndpointRepositoryExposed
import pt.isel.api_pm.repo.exposed.MetricsRepositoryExposed
import pt.isel.api_pm.utils.PasswordHasher
import pt.isel.api_pm.utils.SmtpEmailSender
import java.io.File

class AppDependencies(
    useMemory: Boolean = false,
    useH2: Boolean = true
) {
    private val passwordHasher = PasswordHasher()
    private val jwtService = JwtService()
    private val smtpEmailSender = SmtpEmailSender("example", "example") // TODO: Change this to env variables too

    private val db = if (useMemory) {
        null
    } else {
        val config = createDatabase(useH2)
        val database = DatabaseInitializer.connect(config)
        DatabaseInitializer.init(database)
        database
    }

    private val userRepository = if (useMemory) UserRepositoryMemory() else UserRepositoryExposed(db!!)
    private val metricsRepository = if (useMemory) MetricsRepositoryMemory() else MetricsRepositoryExposed(db!!)
    private val endpointRepo = if (useMemory) EndpointRepositoryMemory() else EndpointRepositoryExposed(db!!)
    private val agentRepo = if (useMemory) AgentRepositoryMemory() else AgentRepositoryMemory() // TODO: Falta em db

    private val sessionManager = AgentSessionManager(agentRepo)

    val userService = UserService(userRepository)
    val authService = AuthService(userRepository, passwordHasher, jwtService)

    val metricsService = MetricsService(metricsRepository)
    val monitoringService = MonitoringService(ktorClient)
    val endpointService = EndpointService(endpointRepo)
    val notificationService = NotificationService(ktorClient, smtpEmailSender)
    val agentService = AgentService(agentRepo, jwtService, sessionManager)

    val alertEvaluator = AlertEvaluator()
}

private fun createDatabase(useH2: Boolean): DatabaseConfig {
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
