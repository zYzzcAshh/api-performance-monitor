package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.repo.UserRepository

class UserRepositoryPostgres : UserRepository {
    override suspend fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: Int): User? {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(username: String, password: String) {
        TODO("Not yet implemented")
    }

    override suspend fun loginUser(username: String, password: String): String {
        TODO("Not yet implemented")
    }
}