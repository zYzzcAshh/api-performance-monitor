package pt.isel.api_pm.utils

import pt.isel.api_pm.domain.user.Password
import pt.isel.api_pm.domain.user.Username
import pt.isel.api_pm.dto.user.LoginRequest
import pt.isel.api_pm.dto.user.RegisterRequest

fun LoginRequest.toCredentials():
        Pair<Username, Password> =
    Pair(
        Username(username),
        Password(password)
    )

fun RegisterRequest.toCredentials():
        Pair<Username, Password> =
    Pair(
        Username(username),
        Password(password)
    )