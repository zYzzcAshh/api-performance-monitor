package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.InvalidPasswordException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher
import pt.isel.api_pm.utils.isValidPassword

class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtService: JwtService,
) {
    suspend fun register(
        username: String,
        password: String,
    ) {
        val existing = userRepository.getUserByUsername(username)
        if (existing != null) throw RegistrationFailedException(username)

        if (!isValidPassword(password)) throw InvalidPasswordException()

        val hashedPassword = passwordHasher.hash(password)

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
