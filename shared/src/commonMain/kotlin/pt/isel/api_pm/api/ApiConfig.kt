package pt.isel.api_pm.api

object ApiConfig {
    const val isLocalhost = false
    var BASE_URL = "http://localhost:8080"

    init {
        // Bad hardcoded approach
        if (!isLocalhost) {
            BASE_URL = "https://api-performance-monitor-cnyy.onrender.com/"
        }
    }
}