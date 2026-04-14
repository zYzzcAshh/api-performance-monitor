package pt.isel.api_pm.routes

object Routes {

    object Agent {
        const val BASE = "/api/agent"
        const val CREATE = "$BASE/register"
    }

    object Auth {
        const val BASE = "/api/auth"

        const val REGISTER = "$BASE/register"
        const val LOGIN = "$BASE/login"
    }

    object Endpoints {
        const val BASE = "/api/endpoints"

        const val CREATE = "$BASE/create"
        const val DELETE = "$BASE/{id}"
    }

    object Metrics {
        const val BASE = "/api/metrics"

        const val CHECK = "$BASE/check"
        const val BY_ENDPOINT = "$BASE/{endpoint}"
        const val SUMMARY = "$BASE/{endpointId}/summary"
    }

    object Users {
        const val BASE = "/api/users"

        const val BY_ID = "$BASE/{id}"
    }

    object Test {
        const val BASE = "/api/test"

        const val GITHUB = "$BASE/github"
        const val OK = "$BASE/ok"
        const val ERROR = "$BASE/error"
        const val NOT_FOUND = "$BASE/notfound"
        const val SLOW = "$BASE/slow"
        const val RANDOM = "$BASE/random"
    }
}