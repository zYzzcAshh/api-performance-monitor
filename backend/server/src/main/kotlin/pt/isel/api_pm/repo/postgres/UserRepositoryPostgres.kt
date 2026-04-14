package pt.isel.api_pm.repo.postgres

import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.repo.UserRepository

class UserRepositoryPostgres : UserRepository {
    override suspend fun getUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(id: UInt): User? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByUsername(username: Username): User? {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun registerUser(
        username: Username,
        passwordHash: PasswordHash,
    ) : User {
        TODO("Not yet implemented")
    }
}
