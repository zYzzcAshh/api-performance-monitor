package pt.isel.api_pm.service

import kotlinx.coroutines.test.runTest
import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.exceptions.BadCredentialsException
import pt.isel.api_pm.exceptions.RegistrationFailedException
import pt.isel.api_pm.repo.memory.UserRepositoryMemory
import pt.isel.api_pm.utils.PasswordHasher
import kotlin.test.*

class AuthServiceTests {

    private val userRepository =
        UserRepositoryMemory()

    private val passwordHasher =
        PasswordHasher()

    private val jwtService =
        JwtService()

    private val service =
        AuthService(
            userRepository,
            passwordHasher,
            jwtService
        )

    @Test
    fun `should register user`() =
        runTest {

            val user =
                service.register(
                    Username("user1"),
                    Password("Password1")
                )

            assertEquals(
                "user1",
                user.username.value
            )
        }

    @Test
    fun `should reject duplicate register`() =
        runTest {

            service.register(
                Username("user1"),
                Password("Password1")
            )

            assertFailsWith<RegistrationFailedException> {

                service.register(
                    Username("user1"),
                    Password("Password1")
                )
            }
        }

    @Test
    fun `should login successfully`() =
        runTest {

            service.register(
                Username("user2"),
                Password("Password1")
            )

            val token =
                service.login(
                    Username("user2"),
                    Password("Password1")
                )

            assertTrue(
                token.isNotBlank()
            )
        }

    @Test
    fun `should reject login with wrong password`() =
        runTest {

            service.register(
                Username("user3"),
                Password("Password1")
            )

            assertFailsWith<BadCredentialsException> {

                service.login(
                    Username("user3"),
                    Password("WrongPassword1")
                )
            }
        }

    @Test
    fun `should reject login with missing user`() =
        runTest {

            assertFailsWith<BadCredentialsException> {

                service.login(
                    Username("ghost"),
                    Password("Password1")
                )
            }
        }
}