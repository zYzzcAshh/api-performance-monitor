package pt.isel.api_pm.utils

import io.ktor.server.auth.jwt.JWTPrincipal
import pt.isel.api_pm.config.AuthConfig
import pt.isel.api_pm.exceptions.InvalidTokenException
import pt.isel.api_pm.exceptions.MissingTokenException

fun JWTPrincipal.requireUserId(): UInt {

    val tokenUserId =
        this.getClaim(
            AuthConfig.USER_ID_CLAIM,
            Int::class
        ) ?: throw InvalidTokenException()

    return tokenUserId.toUInt()
}

fun String?.requireUIntParameter(
    name: String
): UInt {

    return this?.toUIntOrNull()
        ?: throw IllegalArgumentException(
            "Invalid $name"
        )
}

fun String?.requireIntParameter(
    name: String
): Int =
    this?.toIntOrNull()
        ?: throw IllegalArgumentException("Invalid $name")