package pt.isel.api_pm.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import pt.isel.api_pm.utils.AuthConfig
import java.util.Date

class JwtService {
    private val secret = AuthConfig.SECRET
    private val algorithm = Algorithm.HMAC256(secret)
    private val issuer = AuthConfig.ISSUER
    private val validity = 3600000 * 4 // 4 hours
    private val agentValidity = 3600000 * 24

    fun generateToken(userId: UInt): String {
        val now = System.currentTimeMillis()
        return JWT
            .create()
            .withIssuer(issuer)
            .withClaim(AuthConfig.USER_ID_CLAIM, userId.toInt())
            .withExpiresAt(Date(now + validity))
            .sign(algorithm)
    }

    fun generateAgentToken(userId: UInt, agentId: UInt): String {
        val now = System.currentTimeMillis()
        return JWT
            .create()
            .withIssuer(issuer)
            .withClaim(AuthConfig.USER_ID_CLAIM, userId.toInt())
            .withClaim(AuthConfig.AGENT_ID_CLAIM, agentId.toInt())
            .withExpiresAt(Date(now + agentValidity))
            .sign(algorithm)
    }
}
