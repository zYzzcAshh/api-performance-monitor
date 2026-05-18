package pt.isel.api_pm.service

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import kotlin.test.*

class UserServiceTests {

    private fun createService(): UserService {

        val repository =
            UserRepositoryMemory()

        return UserService(repository)
    }

    private val user =
        User(
            id = 1u,
            username = Username("user1"),
            passwordHash = PasswordHash("hashed"),
            createdAt = kotlin.time.Clock.System.now()
        )

    @Test
    fun `should get user by id`() =
        runTest {

            val service =
                createService()

            service.addUser(user)

            val result =
                service.getUserById(1u)

            assertNotNull(result)

            assertEquals(
                "user1",
                result.username.value
            )
        }

    @Test
    fun `should return null for missing user`() =
        runTest {

            val service =
                createService()

            val result =
                service.getUserById(999u)

            assertNull(result)
        }
}