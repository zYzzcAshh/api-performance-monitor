package pt.isel.api_pm.exceptions

class UserAlreadyExistsException(
    username: String,
) : Exception("User with username '$username' already exists")
