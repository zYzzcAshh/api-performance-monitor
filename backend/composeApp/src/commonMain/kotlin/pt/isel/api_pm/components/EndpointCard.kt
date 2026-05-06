package pt.isel.api_pm.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pt.isel.api_pm.theme.Primary
import pt.isel.api_pm.theme.TextSecondary

@Composable
fun EndpointCard(
    title: String,
    url: String,
    interval: String
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = url,
                color = TextSecondary
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Interval: $interval",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}