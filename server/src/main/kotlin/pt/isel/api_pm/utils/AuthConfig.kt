package pt.isel.api_pm.utils

object AuthConfig {
    const val JWT_NAME = "auth-jwt"
    const val USER_ID_CLAIM = "userId"
    const val AGENT_ID_CLAIM = "agentId"

    val SECRET = System.getenv("JWT_SECRET") ?: error("JWT_SECRET environment variable is missing")
    val ISSUER = System.getenv("JWT_ISSUER") ?: error("JWT_ISSUER environment variable is missing")
}