package pt.isel.api_pm.domain.user

class Password(val value: String) {
    init {
        require(value.length >= 6) { "Password must have at least 6 characters" }
        require(value.any { it.isUpperCase() }) { "Password must contain an uppercase letter" }
        require(value.any { it.isDigit() }) { "Password must contain a digit" }
    }
}