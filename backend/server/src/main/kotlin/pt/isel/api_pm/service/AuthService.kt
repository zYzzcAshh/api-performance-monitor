package pt.isel.api_pm.service

import pt.isel.api_pm.repo.UserRepository

class AuthService(
    private val userRepository: UserRepository
) {
    suspend fun register(username: String, password: String) = userRepository.registerUser(username, password)

    suspend fun login(username: String, password: String): String = userRepository.loginUser(username, password)
}