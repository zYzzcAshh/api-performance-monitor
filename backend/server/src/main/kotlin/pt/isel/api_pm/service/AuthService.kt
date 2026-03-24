package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.exceptions.UserNotFoundException
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher

import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.domain.user.PasswordHash

class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val jwtService: JwtService,
) {

    suspend fun register(
        username: String,
        password: String,
    ) {
        val usernameVO = Username(username)
        val passwordVO = Password(password)

        val existing = userRepository.getUserByUsername(usernameVO)
        if (existing != null) throw RegistrationFailedException(username)

        val hashedPassword = passwordHasher.hash(passwordVO.value)
        val passwordHashVO = PasswordHash(hashedPassword)

        userRepository.registerUser(usernameVO, passwordHashVO)
    }

    suspend fun login(
        username: String,
        password: String,
    ): String {
        val usernameVO = Username(username)

        val user =
            userRepository.getUserByUsername(usernameVO)
                ?: throw UserNotFoundException(username)

        if (!passwordHasher.verify(password, user.passwordHash.value)) {
            throw BadCredentialsException()
        }

        return jwtService.generateToken(user.id.value)
    }

    init {
        runBlocking {
            register("admin", "Admin1234")
        }
    }
}
