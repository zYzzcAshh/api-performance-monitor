package pt.isel.api_pm.exceptions

class InvalidPasswordException :
    RuntimeException("Password must have at least 6 characters, one uppercase letter and one number")