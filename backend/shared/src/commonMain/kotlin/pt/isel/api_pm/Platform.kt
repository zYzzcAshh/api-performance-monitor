package pt.isel.api_pm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform