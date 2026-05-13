package pt.isel.api_pm.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.api_pm.api.ApiClient
import pt.isel.api_pm.navigation.AppNavigation
import pt.isel.api_pm.theme.AppTheme

@Composable
@Preview
fun App() {

    val api = remember {
        ApiClient()
    }

    AppTheme {
        AppNavigation(api)
    }
}