package pt.isel.api_pm.repo.exposed

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.user.PasswordHash
import pt.isel.api_pm.domain.user.User
import pt.isel.api_pm.domain.user.Username
import kotlin.test.*
import kotlin.time.Clock

class UserRepositoryExposedTests {

    private fun createRepository(): UserRepositoryExposed {

        TestDatabase.init()

        return UserRepositoryExposed(
            TestDatabase.db
        )
    }

    @Test
    fun `should add user`() =
        runTest {

            val repository =
                createRepository()

            val user =
                User(
                    id = 1u,
                    username = Username("user1"),
                    passwordHash = PasswordHash("hashed-password"),
                    createdAt = Clock.System.now()
                )

            repository.addUser(user)

            val users =
                repository.getUsers()

            assertEquals(
                1,
                users.size
            )

            assertEquals(
                "user1",
                users.first().username.value
            )
        }

    @Test
    fun `should register user`() =
        runTest {

            val repository =
                createRepository()

            val user =
                repository.registerUser(
                    username = Username("user2"),
                    passwordHash = PasswordHash("hashed")
                )

            assertEquals(
                "user2",
                user.username.value
            )

            assertEquals(
                "hashed",
                user.passwordHash.value
            )
        }

    @Test
    fun `should get user by id`() =
        runTest {

            val repository =
                createRepository()

            val created =
                repository.registerUser(
                    Username("user3"),
                    PasswordHash("hashed")
                )

            val user =
                repository.getUserById(
                    created.id
                )

            assertNotNull(user)

            assertEquals(
                "user3",
                user.username.value
            )
        }

    @Test
    fun `should return null for missing user id`() =
        runTest {

            val repository =
                createRepository()

            val user =
                repository.getUserById(999u)

            assertNull(user)
        }

    @Test
    fun `should get user by username`() =
        runTest {

            val repository =
                createRepository()

            repository.registerUser(
                Username("john"),
                PasswordHash("hashed")
            )

            val user =
                repository.getUserByUsername(
                    Username("john")
                )

            assertNotNull(user)

            assertEquals(
                "john",
                user.username.value
            )
        }

    @Test
    fun `should return null for missing username`() =
        runTest {

            val repository =
                createRepository()

            val user =
                repository.getUserByUsername(
                    Username("ghost")
                )

            assertNull(user)
        }

    @Test
    fun `should return all users`() =
        runTest {

            val repository =
                createRepository()

            repository.registerUser(
                Username("user1"),
                PasswordHash("hash1")
            )

            repository.registerUser(
                Username("user2"),
                PasswordHash("hash2")
            )

            val users =
                repository.getUsers()

            assertEquals(
                2,
                users.size
            )
        }
}