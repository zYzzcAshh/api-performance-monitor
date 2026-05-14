package pt.isel.api_pm.exceptions

import pt.isel.api_pm.domain.user.Username

// To be changed by BadCredentials
class RegistrationFailedException(
    username: Username,
) : Exception("User with username '${username.value}' already exists")
