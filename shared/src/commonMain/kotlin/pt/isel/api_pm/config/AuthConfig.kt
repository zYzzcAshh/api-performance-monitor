package pt.isel.api_pm.config

object AuthConfig {
    const val JWT_NAME = "auth-jwt"
    const val SECRET = "api-pm-secret"
    const val ISSUER = "api-pm"
    const val USER_ID_CLAIM = "userId"
}