package pt.isel.api_pm.exceptions

// To be changed by BadCredentials
class RegistrationFailedException(
    username: String,
) : Exception("User with username '$username' already exists")
