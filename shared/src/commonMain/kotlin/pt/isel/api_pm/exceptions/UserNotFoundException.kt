package pt.isel.api_pm.exceptions

// To be removed in the future and replaced by BadCredentialsException, but for now it is used to
// distinguish between user not found and wrong password
class UserNotFoundException(
    username: String,
) : Exception("User with username '$username' not found")
