package pt.isel.api_pm.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import pt.isel.api_pm.theme.Primary

@Composable
fun MetricRow(
    label: String,
    value: String
) {

    Row {

        Text(
            text = "$label: ",
            color = Primary,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = value
        )
    }
}