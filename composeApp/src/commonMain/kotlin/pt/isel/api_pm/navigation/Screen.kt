package pt.isel.api_pm.navigation

sealed class Screen(
    val route: String
) {

    object Login : Screen("login")

    object Register : Screen("register")

    object Endpoints : Screen("endpoints")

    object CreateMonitoring : Screen("create_monitoring")

    object EndpointDashboard : Screen("endpoint_dashboard")

    object Agents : Screen("agents")

    object AgentDashboard : Screen("agent_dashboard")
}