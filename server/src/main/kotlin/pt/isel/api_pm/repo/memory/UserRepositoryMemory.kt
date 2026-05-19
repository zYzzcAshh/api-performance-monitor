package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.repo.UserRepository
import pt.isel.api_pm.utils.PasswordHasher
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class UserRepositoryMemory : UserRepository {
    private val users = ConcurrentHashMap<UInt, User>()


    init {
        val hasher = PasswordHasher()

        val passwordHash = PasswordHash(
            hasher.hash("Admin1234")
        )

        val admin =
            User(
                id = 0u,
                username = Username("admin"),
                passwordHash = passwordHash,
                createdAt = Clock.System.now(),
            )

        users[admin.id] = admin
    }

    override suspend fun getUsers(): List<User> = users.values.toList()

    override suspend fun getUserById(id: UInt): User? = users[id]

    override suspend fun getUserByUsername(username: Username): User? = users.values.find { it.username == username }

    override suspend fun addUser(user: User) {
        users[user.id] = user
    }

    override suspend fun registerUser(
        username: Username,
        passwordHash: PasswordHash,
    ): User {
        val id = users.size.toUInt()
        val createdAt = Clock.System.now()

        val user =
            User(
                id = id,
                username = username,
                passwordHash = passwordHash,
                createdAt = createdAt,
            )

        users[id] = user
        return user
    }
}
