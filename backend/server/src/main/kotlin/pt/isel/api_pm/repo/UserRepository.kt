package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.UserId

interface UserRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUserById(id: UserId): User?

    suspend fun getUserByUsername(username: Username): User?

    suspend fun addUser(user: User)

    suspend fun registerUser(
        username: Username,
        passwordHash: PasswordHash,
    )
}
