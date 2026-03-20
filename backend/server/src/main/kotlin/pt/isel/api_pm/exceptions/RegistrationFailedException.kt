package pt.isel.api_pm.exceptions


class RegistrationFailedException(
    username: String,
) : Exception("User with username '$username' already exists")
