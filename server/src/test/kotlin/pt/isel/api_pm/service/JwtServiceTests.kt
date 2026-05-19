package pt.isel.api_pm.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import pt.isel.api_pm.utils.AuthConfig
import kotlin.test.*

class JwtServiceTests {

    private val service =
        JwtService()

    @Test
    fun `should generate token`() {

        val token =
            service.generateToken(1u)

        assertTrue(
            token.isNotBlank()
        )
    }

    @Test
    fun `should generate token with correct user id`() {

        val token =
            service.generateToken(42u)

        val decoded =
            JWT.decode(token)

        val userId =
            decoded.getClaim(AuthConfig.USER_ID_CLAIM)
                .asInt()

        assertEquals(
            42,
            userId
        )
    }

    @Test
    fun `should generate token with correct issuer`() {

        val token =
            service.generateToken(1u)

        val decoded =
            JWT.decode(token)

        assertEquals(
            AuthConfig.ISSUER,
            decoded.issuer
        )
    }

    @Test
    fun `should generate valid signed token`() {

        val token =
            service.generateToken(1u)

        val verifier =
            JWT
                .require(
                    Algorithm.HMAC256(AuthConfig.SECRET)
                )
                .withIssuer(AuthConfig.ISSUER)
                .build()

        val decoded =
            verifier.verify(token)

        assertEquals(
            1,
            decoded.getClaim(AuthConfig.USER_ID_CLAIM)
                .asInt()
        )
    }
}