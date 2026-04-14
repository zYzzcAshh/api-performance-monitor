package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher

class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtService: JwtService,
) {
    suspend fun register(
        username: Username,
        password: Password,
    ) {
        val existing = userRepository.getUserByUsername(username)
        if (existing != null) throw RegistrationFailedException(username)

        val hashedPassword = passwordHasher.hash(password.value)
        val passwordHashVO = PasswordHash(hashedPassword)

        userRepository.registerUser(username, passwordHashVO)
    }

    suspend fun login(
        username: Username,
        password: Password,
    ): String {
        val user =
            userRepository.getUserByUsername(username)
                ?: throw UserNotFoundException(username.value)

        if (!passwordHasher.verify(password.value, user.passwordHash.value)) {
            throw BadCredentialsException()
        }

        return jwtService.generateToken(user.id)
    }

    init {
        runBlocking {
            register(Username("admin"), Password("Admin1234"))
        }
    }
}
