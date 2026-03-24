package pt.isel.api_pm.service

import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.UserId
import pt.isel.api_pm.repo.UserRepository

class UserService(
    private val repository: UserRepository,
) {
    suspend fun getUsers() = repository.getUsers()

    suspend fun getUserById(id: UserId) =
        repository.getUserById(id)

    suspend fun addUser(user: User) = repository.addUser(user)
}
