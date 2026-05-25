package pt.isel.api_pm.repo.memory

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import kotlin.test.*

class UserRepositoryMemoryTests {

    private fun createRepository() =
        UserRepositoryMemory()

    private fun user(
        id: UInt = 1u,
        username: String = "user1"
    ) =
        User(
            id = id,
            username = Username(username),
            passwordHash = PasswordHash("hashed-password"),
            createdAt = kotlin.time.Clock.System.now()
        )

    @Test
    fun `should contain admin by default`() =
        runTest {

            val repository =
                createRepository()

            val users =
                repository.getUsers()

            assertEquals(
                1,
                users.size
            )

            assertEquals(
                "admin",
                users.first().username.value
            )
        }

    @Test
    fun `should add user`() =
        runTest {

            val repository =
                createRepository()

            repository.addUser(
                user()
            )

            val users =
                repository.getUsers()

            assertEquals(
                2,
                users.size
            )
        }

    @Test
    fun `should get user by id`() =
        runTest {

            val repository =
                createRepository()

            repository.addUser(
                user()
            )

            val result =
                repository.getUserById(1u)

            assertNotNull(result)

            assertEquals(
                "user1",
                result.username.value
            )
        }

    @Test
    fun `should return null for missing user id`() =
        runTest {

            val repository =
                createRepository()

            val result =
                repository.getUserById(999u)

            assertNull(result)
        }

    @Test
    fun `should get user by username`() =
        runTest {

            val repository =
                createRepository()

            repository.addUser(
                user()
            )

            val result =
                repository.getUserByUsername(
                    Username("user1")
                )

            assertNotNull(result)

            assertEquals(
                1u,
                result.id
            )
        }

    @Test
    fun `should return null for missing username`() =
        runTest {

            val repository =
                createRepository()

            val result =
                repository.getUserByUsername(
                    Username("ghost")
                )

            assertNull(result)
        }

    @Test
    fun `should register user`() =
        runTest {

            val repository =
                createRepository()

            val registered =
                repository.registerUser(
                    username = Username("new-user"),
                    passwordHash = PasswordHash("hashed")
                )

            assertEquals(
                1u,
                registered.id
            )

            assertEquals(
                "new-user",
                registered.username.value
            )

            val users =
                repository.getUsers()

            assertEquals(
                2,
                users.size
            )
        }

    @Test
    fun `should generate incremental ids when registering users`() =
        runTest {

            val repository =
                createRepository()

            val first =
                repository.registerUser(
                    Username("user1"),
                    PasswordHash("hash1")
                )

            val second =
                repository.registerUser(
                    Username("user2"),
                    PasswordHash("hash2")
                )

            assertEquals(
                1u,
                first.id
            )

            assertEquals(
                2u,
                second.id
            )
        }
}