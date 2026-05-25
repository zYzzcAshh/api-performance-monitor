package pt.isel.api_pm.utils

import io.ktor.server.auth.jwt.*
import pt.isel.api_pm.exceptions.InvalidTokenException

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