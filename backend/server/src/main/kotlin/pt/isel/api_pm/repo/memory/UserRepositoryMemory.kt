package pt.isel.api_pm.repo.memory

import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.repo.UserRepository
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Clock

class UserRepositoryMemory : UserRepository {
    private val users = ConcurrentHashMap<Int, User>()

    override suspend fun getUsers(): List<User> = users.values.toList()

    override suspend fun getUserById(id: Int): User? = users[id]

    override suspend fun getUserByUsername(username: String): User? = users.values.find { it.username == username }

    override suspend fun addUser(user: User) {
        users[user.id] = user
    }

    override suspend fun registerUser(
        username: String,
        password: String,
    ) {
        val id = users.size
        val createdAt = Clock.System.now()
        val user = User(id, username, password, createdAt)
        users[id] = user
    }
}
