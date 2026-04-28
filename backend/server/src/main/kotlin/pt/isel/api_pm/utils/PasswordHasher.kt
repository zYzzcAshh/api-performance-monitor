package pt.isel.api_pm.utils

import com.password4j.Password
import com.password4j.SecureString

class PasswordHasher {
    fun hash(password: String): String =
        Password.hash(SecureString(password.toCharArray()))
            .withBcrypt().result

    fun verify(password: String, hashed: String): Boolean =
        Password.check(SecureString(password.toCharArray()), hashed)
            .withBcrypt()
}
