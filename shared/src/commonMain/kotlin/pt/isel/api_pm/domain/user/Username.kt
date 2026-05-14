package pt.isel.api_pm.domain.user

import kotlin.jvm.JvmInline

@JvmInline
value class Username(val value: String) {
    init {
        require(value.length >= 3) { "Username must have at least 3 characters" }
    }
}