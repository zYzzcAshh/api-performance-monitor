package pt.isel.api_pm

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Api Performance Monitor",
    ) {
        App()
    }
}