package pt.isel.api_pm.screen

sealed class Screen(val route: String) {

    object Login : Screen("login")

    object Register : Screen("register")

    object Endpoints : Screen("endpoints")

    object EndpointDetails : Screen("endpoint_details")
}