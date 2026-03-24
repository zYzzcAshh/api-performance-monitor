package pt.isel.api_pm.utils

fun isValidPassword(password: String): Boolean {
    val hasMinLength = password.length >= 6
    val hasUpperCase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }

    return hasMinLength && hasUpperCase && hasDigit
}
