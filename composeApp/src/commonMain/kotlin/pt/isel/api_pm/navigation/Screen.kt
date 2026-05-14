package pt.isel.api_pm.navigation

sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Register : Screen("register")

    object Endpoints : Screen("endpoints")

    object EndpointDetails : Screen("endpoint_details")
}