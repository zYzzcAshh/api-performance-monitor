package pt.isel.api_pm.exceptions

class UserNotFoundException(
    username: String,
) : Exception("User with username '$username' not found")
