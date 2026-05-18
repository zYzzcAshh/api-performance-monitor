package pt.isel.api_pm.service

import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.RegistrationFailedException
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
    ): User {
        val existing = userRepository.getUserByUsername(username)
        if (existing != null) throw RegistrationFailedException(username)

        val hashedPassword = passwordHasher.hash(password.value)
        val passwordHashVO = PasswordHash(hashedPassword)

        return userRepository.registerUser(username, passwordHashVO)
    }

    suspend fun login(
        username: Username,
        password: Password,
    ): String {
        val user =
            userRepository.getUserByUsername(username)
                ?: throw BadCredentialsException()

        if (!passwordHasher.verify(password.value, user.passwordHash.value)) {
            throw BadCredentialsException()
        }

        return jwtService.generateToken(user.id)
    }
}
