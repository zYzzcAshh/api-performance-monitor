package pt.isel.api_pm.service

import kotlinx.coroutines.runBlocking
import pt.isel.api_pm.exceptions.*
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import pt.isel.api_pm.utils.PasswordHasher
import kotlin.test.*

class AuthServiceTests {
    private lateinit var service: AuthService

    @BeforeTest
    fun setup() {
        val repo = UserRepositoryMemory()
        val hasher = PasswordHasher()
        val jwt = JwtService()

        service = AuthService(repo, hasher, jwt)
    }

    @Test
    fun `should register user successfully`() =
        runBlocking {
            service.register("user1", "Password1")
        }

    @Test
    fun `should reject duplicate username`() {
        runBlocking {
            service.register("user1", "Password1")

            assertFailsWith<RegistrationFailedException> {
                service.register("user1", "Password1")
            }
        }
    }

    @Test
    fun `should reject invalid password`() {
        runBlocking {
            val ex = assertFailsWith<IllegalArgumentException> {
                service.register("user2", "abc")
            }
            assertNotNull(ex.message)        }
    }

    @Test
    fun `should login successfully`() =
        runBlocking {
            service.register("user3", "Password1")

            val token = service.login("user3", "Password1")

            assertTrue(token.isNotEmpty())
        }

    @Test
    fun `should reject wrong password`() {
        runBlocking {
            service.register("user4", "Password1")

            assertFailsWith<BadCredentialsException> {
                service.login("user4", "wrong")
            }
        }
    }

    @Test
    fun `should reject password without uppercase`() {
        runBlocking {
            val ex = assertFailsWith<IllegalArgumentException> {
                service.register("userX", "password1")
            }
            assertNotNull(ex.message)        }
    }

    @Test
    fun `should reject password without digit`() {
        runBlocking {
            val ex = assertFailsWith<IllegalArgumentException> {
                service.register("userY", "Password")
            }
            assertNotNull(ex.message)        }
    }

    @Test
    fun `should reject login with non existing user`() {
        runBlocking {
            assertFailsWith<UserNotFoundException> {
                service.login("ghost", "Password1")
            }
        }
    }
}
