package pt.isel.api_pm.routes

object Routes {

    object Agent {
        const val BASE = "/agent"
        const val CREATE = "$BASE/register"
    }

    object Auth {
        const val BASE = "/auth"

        const val REGISTER = "$BASE/register"
        const val LOGIN = "$BASE/login"
    }

    object Endpoints {
        const val BASE = "/endpoints"

        const val DELETE = "$BASE/{id}"
    }

    object Metrics {
        const val BASE = "/metrics"

        const val CHECK = "$BASE/check"
        const val BY_ENDPOINT = "$BASE/{endpoint}"
        const val SUMMARY = "$BASE/{endpointId}/summary"
    }

    object Users {
        const val BASE = "/users"

        const val BY_ID = "$BASE/{id}"
    }

    object Test {
        const val BASE = "/test"

        const val OK = "/ok"
        const val ERROR = "/error"
        const val NOT_FOUND = "/notfound"
        const val SLOW = "/slow"
        const val RANDOM = "/random"
        const val GITHUB = "/github"
    }
}