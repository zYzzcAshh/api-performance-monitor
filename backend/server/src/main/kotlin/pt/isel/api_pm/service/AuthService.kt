package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.InvalidPasswordException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher

class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtService: JwtService,
) {
    private fun isValidPassword(password: String): Boolean {
        val hasMinLength = password.length >= 6
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasDigit = password.any { it.isDigit() }

        return hasMinLength && hasUpperCase && hasDigit
    }

    suspend fun register(
        username: String,
        password: String,
    ) {
        val existing = userRepository.getUserByUsername(username)
        if (existing != null) throw RegistrationFailedException(username)

        val hashedPassword = passwordHasher.hash(password)

        if (!isValidPassword(password)) {
            throw InvalidPasswordException()
        }

        userRepository.registerUser(username, hashedPassword)
    }

    suspend fun login(
        username: String,
        password: String,
    ): String {
        val user = userRepository.getUserByUsername(username) ?: throw UserNotFoundException(username)
        if (!passwordHasher.verify(password, user.passwordHash)) throw BadCredentialsException()
        return jwtService.generateToken(user.id)
    }

    init {
        runBlocking {
            register("admin", "Admin1234")
        }
    }
}
