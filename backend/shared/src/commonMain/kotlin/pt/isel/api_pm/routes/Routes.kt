package pt.isel.api_pm.routes

object Routes {
    object Auth {
        const val BASE = "/api/auth"
        const val REGISTER = "/register"
        const val LOGIN = "/login"
    }

    object Endpoints {
        const val BASE = "/api/endpoints"
        const val CREATE = "/create"
        const val DELETE = "/{id}"
    }

    object Metrics {
        const val BASE = "/api/metrics"
        const val CHECK = "/check"
        const val BY_ENDPOINT = "/{endpoint}"
        const val SUMMARY = "/{endpointId}/summary"
    }

    object Users {
        const val BASE = "/api/users"
        const val BY_ID = "/{id}"
    }

    object Test {
        const val BASE = "/api/test"
        const val GITHUB = "/github"
        const val OK = "/ok"
        const val ERROR = "/error"
        const val NOT_FOUND = "/notfound"
        const val SLOW = "/slow"
        const val RANDOM = "/random"
    }
}