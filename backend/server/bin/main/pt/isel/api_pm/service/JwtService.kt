package pt.isel.api_pm.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

class JwtService {
    private val secret = "api-pm-secret"
    private val algorithm = Algorithm.HMAC256(secret)
    private val issuer = "api-pm"
    private val validity = 3600000 * 4 // 4 hours

    fun generateToken(userId: Int): String {
        val now = System.currentTimeMillis()
        return JWT
            .create()
            .withIssuer(issuer)
            .withClaim("userId", userId)
            .withExpiresAt(Date(now + validity))
            .sign(algorithm)
    }
}
