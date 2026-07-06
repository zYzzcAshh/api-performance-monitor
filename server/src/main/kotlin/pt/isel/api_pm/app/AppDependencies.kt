package pt.isel.api_pm.app

import pt.isel.api_pm.alert.AlertEvaluator
import pt.isel.api_pm.database.DatabaseInitializer
import pt.isel.api_pm.manager.AgentSessionManager
import pt.isel.api_pm.repo.exposed.AgentRepositoryExposed
import pt.isel.api_pm.repo.exposed.EndpointRepositoryExposed
import pt.isel.api_pm.repo.exposed.MetricsRepositoryExposed
import pt.isel.api_pm.repo.exposed.UserRepositoryExposed
import pt.isel.api_pm.repo.memory.AgentRepositoryMemory
import pt.isel.api_pm.repo.memory.EndpointRepositoryMemory
import pt.isel.api_pm.repo.memory.MetricsRepositoryMemory
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import pt.isel.api_pm.service.AgentService
import pt.isel.api_pm.service.AuthService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.JwtService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.NotificationService
import pt.isel.api_pm.service.UserService
import pt.isel.api_pm.utils.AlertPipeline
import pt.isel.api_pm.utils.CooldownManager
import pt.isel.api_pm.utils.PasswordHasher
import pt.isel.api_pm.utils.SmtpConfig
import pt.isel.api_pm.utils.SmtpEmailSender

class AppDependencies(
    useMemory: Boolean = false,
    useH2: Boolean = true
) {

    private val passwordHasher = PasswordHasher()

    private val jwtService = JwtService()

    private val smtpEmailSender =
        SmtpEmailSender(
            SmtpConfig.USER,
            SmtpConfig.PASSWORD
        )

    private val db =
        if (!useMemory) {

            val config =
                DatabaseInitializer.createDatabase(useH2)

            DatabaseInitializer.connect(config).also {
                DatabaseInitializer.init(it)
            }

        } else {
            null
        }

    private val userRepository =
        if (useMemory)
            UserRepositoryMemory()
        else
            UserRepositoryExposed(requireNotNull(db))

    private val metricsRepository =
        if (useMemory)
            MetricsRepositoryMemory()
        else
            MetricsRepositoryExposed(requireNotNull(db))

    private val endpointRepo =
        if (useMemory)
            EndpointRepositoryMemory()
        else
            EndpointRepositoryExposed(requireNotNull(db))

    private val agentRepo =
        if (useMemory)
            AgentRepositoryMemory()
        else
            AgentRepositoryExposed(requireNotNull(db))

    val userService =
        UserService(userRepository)

    val authService =
        AuthService(
            userRepository,
            passwordHasher,
            jwtService
        )

    val metricsService =
        MetricsService(metricsRepository)

    val monitoringService =
        MonitoringService(ktorClient)

    val endpointService =
        EndpointService(endpointRepo)

    val notificationService =
        NotificationService(
            ktorClient,
            smtpEmailSender
        )

    val agentService =
        AgentService(
            agentRepo,
            jwtService
        )

    val alertEvaluator =
        AlertEvaluator()

    val cooldownManager = CooldownManager()

    val alertPipeline = AlertPipeline(
        alertEvaluator,
        notificationService,
        cooldownManager
    )

    val agentSessionManager = AgentSessionManager(metricsService, agentService, alertPipeline)
}