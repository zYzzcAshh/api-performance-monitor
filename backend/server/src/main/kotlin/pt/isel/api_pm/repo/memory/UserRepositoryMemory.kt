package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.UserId
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.repo.UserRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class UserRepositoryMemory : UserRepository {
    private val users = ConcurrentHashMap<Int, User>()

    override suspend fun getUsers(): List<User> = users.values.toList()

    override suspend fun getUserById(id: UserId): User? =
        users[id.value]

    override suspend fun getUserByUsername(username: Username): User? =
        users.values.find { it.username == username }

    override suspend fun addUser(user: User) {
        users[user.id.value] = user
    }

    override suspend fun registerUser(
        username: Username,
        passwordHash: PasswordHash,
    ) {
        val id = users.size
        val createdAt = Clock.System.now()

        val user =
            User(
                id = UserId(id),
                username = username,
                passwordHash = passwordHash,
                createdAt = createdAt,
            )

        users[id] = user
    }
}
