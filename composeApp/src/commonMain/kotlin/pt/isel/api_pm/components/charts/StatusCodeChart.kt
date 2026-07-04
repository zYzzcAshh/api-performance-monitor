package pt.isel.api_pm.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

// Separador de milhares sem usar String.format (evita o problema de "Unresolved reference" que tivemos no outro ficheiro)
private fun formatCount(value: Long): String {
    val reversedChunks = value.toString().reversed().chunked(3)
    return reversedChunks.joinToString(",") { it }.reversed()
}

// Traduz o codigo HTTP num rotulo legivel; cai para a categoria generica se nao for reconhecido
private fun statusLabel(code: Int): String = when (code) {
    200 -> "OK"
    201 -> "Created"
    202 -> "Accepted"
    204 -> "No Content"
    301 -> "Moved Permanently"
    302 -> "Found"
    304 -> "Not Modified"
    400 -> "Bad Request"
    401 -> "Unauthorized"
    403 -> "Forbidden"
    404 -> "Not Found"
    405 -> "Method Not Allowed"
    409 -> "Conflict"
    422 -> "Unprocessable Entity"
    429 -> "Too Many Requests"
    500 -> "Internal Server Error"
    502 -> "Bad Gateway"
    503 -> "Service Unavailable"
    504 -> "Gateway Timeout"
    else -> when (code) {
        in 200..299 -> "Success"
        in 300..399 -> "Redirect"
        in 400..499 -> "Client Error"
        in 500..599 -> "Server Error"
        else -> "Unknown"
    }
}

private fun statusColor(code: Int): Color = when (code) {
    in 200..299 -> Color(0xFF22C55E)
    in 300..399 -> Color(0xFFF59E0B)
    in 400..499 -> Color(0xFFEF4444)
    else -> Color(0xFFDC2626)
}

@Composable
fun StatusCodeChart(
    distribution: Map<Int, Long>
) {

    val max = distribution.values.maxOrNull()?.toFloat() ?: 1f
    val total = distribution.values.sum()
    val errorCount = distribution.entries.filter { it.key >= 400 }.sumOf { it.value }
    val errorRate = if (total > 0) errorCount.toFloat() / total * 100f else 0f
    val errorColor = when {
        errorRate >= 10f -> Color(0xFFDC2626)
        errorRate >= 2f -> Color(0xFFF59E0B)
        else -> Color(0xFF22C55E)
    }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "HTTP Status Codes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "${formatCount(total)} requests",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (total > 0L) {
                    Text(
                        "${errorRate.roundToInt()}% errors",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = errorColor
                    )
                }
            }

            HorizontalDivider()

            if (distribution.isEmpty()) {
                Text(
                    "No data yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
            } else {
                distribution
                    .entries
                    .sortedBy { it.key }
                    .forEachIndexed { index, (code, count) ->

                        val ratio = (count.toFloat() / max).coerceIn(0f, 1f)
                        val percentage = if (total > 0) count.toFloat() / total * 100f else 0f
                        val color = statusColor(code)

                        StatusCodeRow(
                            code = code,
                            count = count,
                            ratio = ratio,
                            percentage = percentage,
                            color = color,
                            delayMillis = index * 60
                        )
                    }
            }
        }
    }
}

@Composable
private fun StatusCodeRow(
    code: Int,
    count: Long,
    ratio: Float,
    percentage: Float,
    color: Color,
    delayMillis: Int
) {

    val animatedRatio = remember { Animatable(0f) }

    LaunchedEffect(ratio) {
        animatedRatio.animateTo(
            targetValue = ratio,
            animationSpec = tween(durationMillis = 700, delayMillis = delayMillis, easing = FastOutSlowInEasing)
        )
    }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("HTTP $code", fontWeight = FontWeight.SemiBold)

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(color.copy(alpha = 0.12f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        statusLabel(code),
                        style = MaterialTheme.typography.labelSmall,
                        color = color,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(formatCount(count), fontWeight = FontWeight.SemiBold)
                Text(
                    "(${percentage.roundToInt()}%)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(50))
                .background(color.copy(alpha = 0.15f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedRatio.value.coerceIn(0f, 1f))
                    .height(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.horizontalGradient(
                            listOf(color.copy(alpha = 0.7f), color)
                        )
                    )
            )
        }
    }
}