package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.repo.UserRepository
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.Instant.Companion.fromEpochMilliseconds

class UserRepositoryMemory : UserRepository {
    private val users = HashMap<Int, User>()

    override suspend fun getUsers(): List<User> {
        return users.values.toList()
    }

    override suspend fun getUserById(id: Int): User? {
        return users[id]
    }

    override suspend fun addUser(user: User) {
        users[user.id] = user
    }

    override suspend fun registerUser(username: String, password: String) {
        val id = users.size
        val createdAt = Clock.System.now()
        val user = User(id, username, password, createdAt)
        users[id] = user
    }

    override suspend fun loginUser(username: String, password: String): String {
        val user = users.values.find { it.username == username && it.passwordHash == password }
        return if (user != null) {
            "token-${user.id}"
        } else {
            "not token"
        }
    }

    init {
        users[0] = User(0, "admin", "admin", Clock.System.now())
    }
}