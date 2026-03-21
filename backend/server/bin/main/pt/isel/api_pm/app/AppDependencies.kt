package pt.isel.api_pm.app

import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.repo.memory.EndpointRepositoryMemory
import pt.isel.api_pm.repo.memory.MetricsRepositoryMemory
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import pt.isel.api_pm.repo.postgres.EndpointRepositoryPostgres
import pt.isel.api_pm.repo.postgres.MetricsRepositoryPostgres
import pt.isel.api_pm.repo.postgres.UserRepositoryPostgres
import pt.isel.api_pm.service.AuthService
import pt.isel.api_pm.service.EndpointService
import pt.isel.api_pm.service.JwtService
import pt.isel.api_pm.service.MetricsService
import pt.isel.api_pm.service.MonitoringService
import pt.isel.api_pm.service.UserService
import pt.isel.api_pm.utils.PasswordHasher

class AppDependencies(
    useMemory: Boolean = true,
) {
    private val passwordHasher: PasswordHasher = PasswordHasher()
    private val jwtService: JwtService = JwtService()

    private val userRepository: UserRepository = if (useMemory) UserRepositoryMemory() else UserRepositoryPostgres()
    private val metricsRepository = if (useMemory) MetricsRepositoryMemory() else MetricsRepositoryPostgres()
    private val endpointRepo = if (useMemory) EndpointRepositoryMemory() else EndpointRepositoryPostgres()

    val userService: UserService = UserService(userRepository)
    val authService: AuthService = AuthService(userRepository, passwordHasher, jwtService)

    val metricsService = MetricsService(metricsRepository)
    val monitoringService = MonitoringService(ktorClient)
    val endpointService = EndpointService(endpointRepo)
}
