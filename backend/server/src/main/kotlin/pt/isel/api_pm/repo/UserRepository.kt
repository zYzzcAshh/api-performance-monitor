package pt.isel.api_pm.repo

import pt.isel.api_pm.domain.user.User

interface UserRepository {
    suspend fun getUsers(): List<User>

    suspend fun getUserById(id: Int): User?

    suspend fun getUserByUsername(username: String): User?

    suspend fun addUser(user: User)

    suspend fun registerUser(
        username: String,
        password: String,
    )
}
