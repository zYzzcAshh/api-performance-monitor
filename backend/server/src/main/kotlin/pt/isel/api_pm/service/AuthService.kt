package pt.isel.api_pm.service

import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.UserAlreadyExistsException
import pt.isel.api_pm.exceptions.UserNotFoundException
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher

class AuthService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
) {
    suspend fun register(
        username: String,
        password: String,
    ) {
        val existing = userRepository.getUserByUsername(username)
        if (existing != null) throw UserAlreadyExistsException(username)

        val hashedPassword = passwordHasher.hash(password)
        userRepository.registerUser(username, hashedPassword)
    }

    suspend fun login(
        username: String,
        password: String,
    ): String {
        val user = userRepository.getUserByUsername(username) ?: throw UserNotFoundException(username)
        if (!passwordHasher.verify(password, user.passwordHash)) throw BadCredentialsException()
        return userRepository.loginUser(username, user.passwordHash)
    }
}
