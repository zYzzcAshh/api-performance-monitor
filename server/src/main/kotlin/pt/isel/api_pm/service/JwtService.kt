package pt.isel.api_pm.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import pt.isel.api_pm.config.AuthConfig
import java.util.Date

class JwtService {
    private val secret = AuthConfig.SECRET
    private val algorithm = Algorithm.HMAC256(secret)
    private val issuer = AuthConfig.ISSUER
    private val validity = 3600000 * 4 // 4 hours

    fun generateToken(userId: UInt): String {
        val now = System.currentTimeMillis()
        return JWT
            .create()
            .withIssuer(issuer)
            .withClaim(AuthConfig.USER_ID_CLAIM, userId.toInt())
            .withExpiresAt(Date(now + validity))
            .sign(algorithm)
    }
}
