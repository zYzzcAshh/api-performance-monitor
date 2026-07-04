package pt.isel.api_pm.components.charts

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.api_pm.dto.metric.RequestMetric
import kotlin.math.abs
import kotlin.math.roundToInt

// Cores de estado consoante a saude da latencia — ajusta os thresholds a vontade
private val ColorHealthy = Color(0xFF34C77B)
private val ColorWarning = Color(0xFFFFB020)
private val ColorCritical = Color(0xFFFF5A5F)

private fun statusColor(avgLatency: Float, warningAt: Float, criticalAt: Float): Color = when {
    avgLatency >= criticalAt -> ColorCritical
    avgLatency >= warningAt -> ColorWarning
    else -> ColorHealthy
}

private fun formatLatency(value: Float): String =
    if (value >= 1000f) "${(value / 1000f * 100).roundToInt() / 100.0}s"
    else "${value.roundToInt()}ms"

@Composable
fun LatencyChart(
    metrics: List<RequestMetric>,
    warningThresholdMs: Float = 300f,
    criticalThresholdMs: Float = 600f
) {

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(modifier = Modifier.padding(20.dp)) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    "Latency",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (metrics.isNotEmpty()) {
                    val avg = metrics.map { it.latency.toFloat() }.average().toFloat()
                    val color = statusColor(avg, warningThresholdMs, criticalThresholdMs)
                    Text(
                        "${formatLatency(avg)} avg",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = color
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

            if (metrics.size < 2) {
                Text(
                    "Sem dados suficientes ainda",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 32.dp)
                )
                return@Column
            }

            val minLatency = metrics.minOf { it.latency.toFloat() }
            val maxLatency = metrics.maxOf { it.latency.toFloat() }
            val avgLatency = metrics.map { it.latency.toFloat() }.average().toFloat()
            val lineColor = statusColor(avgLatency, warningThresholdMs, criticalThresholdMs)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                StatChip("Min", formatLatency(minLatency), ColorHealthy)
                StatChip("Avg", formatLatency(avgLatency), lineColor)
                StatChip("Max", formatLatency(maxLatency), ColorCritical)
            }

            var selectedIndex by remember { mutableStateOf<Int?>(null) }
            val progress = remember { Animatable(0f) }

            LaunchedEffect(metrics) {
                progress.snapTo(0f)
                progress.animateTo(1f, animationSpec = tween(900, easing = FastOutSlowInEasing))
            }

            val textMeasurer = rememberTextMeasurer()

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {

                val density = LocalDensity.current
                val widthPx = with(density) { maxWidth.toPx() }
                val heightPx = with(density) { maxHeight.toPx() }

                val horizontalPadding = with(density) { 8.dp.toPx() }
                val topPadding = with(density) { 16.dp.toPx() }
                val bottomPadding = with(density) { 28.dp.toPx() }

                val graphWidth = widthPx - horizontalPadding * 2
                val graphHeight = heightPx - topPadding - bottomPadding
                val spacing = graphWidth / (metrics.size - 1)
                val range = (maxLatency - minLatency).takeIf { it > 0f } ?: 1f

                val points = remember(metrics, widthPx, heightPx) {
                    metrics.mapIndexed { index, metric ->
                        val x = horizontalPadding + index * spacing
                        val normalized = (metric.latency.toFloat() - minLatency) / range
                        val y = topPadding + graphHeight - normalized * graphHeight
                        Offset(x, y)
                    }
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(points) {
                            detectDragGestures(
                                onDragStart = { offset -> selectedIndex = nearestIndex(offset, points) },
                                onDrag = { change, _ ->
                                    selectedIndex = nearestIndex(change.position, points)
                                    change.consume()
                                },
                                onDragEnd = { selectedIndex = null }
                            )
                        }
                        .pointerInput(points) {
                            detectTapGestures(
                                onPress = { offset ->
                                    selectedIndex = nearestIndex(offset, points)
                                    tryAwaitRelease()
                                    selectedIndex = null
                                }
                            )
                        }
                ) {

                    //----------------------------------
                    // GRID (tracejado, mais discreto)
                    //----------------------------------

                    repeat(4) { index ->
                        val y = topPadding + graphHeight / 3f * index
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.2f),
                            start = Offset(horizontalPadding, y),
                            end = Offset(widthPx - horizontalPadding, y),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f))
                        )
                    }

                    //----------------------------------
                    // LINHA + AREA (com animacao de entrada)
                    //----------------------------------

                    clipRect(right = horizontalPadding + graphWidth * progress.value) {

                        val smoothLine = smoothPath(points)

                        val areaPath = Path().apply {
                            addPath(smoothLine)
                            lineTo(points.last().x, topPadding + graphHeight)
                            lineTo(points.first().x, topPadding + graphHeight)
                            close()
                        }

                        drawPath(
                            path = areaPath,
                            brush = Brush.verticalGradient(
                                listOf(lineColor.copy(alpha = 0.30f), Color.Transparent)
                            )
                        )

                        // glow por baixo da linha
                        drawPath(
                            path = smoothLine,
                            color = lineColor.copy(alpha = 0.25f),
                            style = Stroke(width = 14f, cap = StrokeCap.Round)
                        )

                        drawPath(
                            path = smoothLine,
                            color = lineColor,
                            style = Stroke(width = 5f, cap = StrokeCap.Round)
                        )
                    }

                    //----------------------------------
                    // PONTOS
                    //----------------------------------

                    points.forEachIndexed { index, point ->
                        if (points.size == 1 || index.toFloat() / (points.size - 1) <= progress.value) {
                            val isSelected = index == selectedIndex
                            drawCircle(Color.White, radius = if (isSelected) 8f else 6f, center = point)
                            drawCircle(lineColor, radius = if (isSelected) 6f else 4f, center = point)
                        }
                    }

                    //----------------------------------
                    // TOOLTIP (toque / arrastar)
                    //----------------------------------

                    selectedIndex?.let { index ->
                        val point = points[index]
                        val metric = metrics[index]

                        drawLine(
                            color = lineColor.copy(alpha = 0.4f),
                            start = Offset(point.x, topPadding),
                            end = Offset(point.x, topPadding + graphHeight),
                            strokeWidth = 2f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f))
                        )

                        val label = formatLatency(metric.latency.toFloat())
                        val textStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        val textLayout = textMeasurer.measure(label, style = textStyle)

                        val bubbleWidth = textLayout.size.width + 24
                        val bubbleHeight = textLayout.size.height + 12
                        val bubbleX = (point.x - bubbleWidth / 2f).coerceIn(0f, widthPx - bubbleWidth)
                        val bubbleY = (point.y - bubbleHeight - 12f).coerceAtLeast(0f)

                        drawRoundRect(
                            color = lineColor,
                            topLeft = Offset(bubbleX, bubbleY),
                            size = Size(bubbleWidth.toFloat(), bubbleHeight.toFloat()),
                            cornerRadius = CornerRadius(10f, 10f)
                        )

                        drawText(
                            textMeasurer = textMeasurer,
                            text = label,
                            topLeft = Offset(bubbleX + 12f, bubbleY + 6f),
                            style = textStyle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatChip(label: String, value: String, color: Color) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

private fun nearestIndex(offset: Offset, points: List<Offset>): Int {
    var closest = 0
    var minDist = Float.MAX_VALUE
    points.forEachIndexed { index, point ->
        val dist = abs(point.x - offset.x)
        if (dist < minDist) {
            minDist = dist
            closest = index
        }
    }
    return closest
}

// Catmull-Rom -> Bezier: transforma os pontos numa curva suave em vez de linhas retas
private fun smoothPath(points: List<Offset>): Path {
    val path = Path()
    if (points.isEmpty()) return path
    path.moveTo(points[0].x, points[0].y)
    if (points.size == 1) return path

    for (i in 0 until points.size - 1) {
        val p0 = points[if (i > 0) i - 1 else 0]
        val p1 = points[i]
        val p2 = points[i + 1]
        val p3 = points[if (i != points.size - 2) i + 2 else i + 1]

        val cp1x = p1.x + (p2.x - p0.x) / 6f
        val cp1y = p1.y + (p2.y - p0.y) / 6f
        val cp2x = p2.x - (p3.x - p1.x) / 6f
        val cp2y = p2.y - (p3.y - p1.y) / 6f

        path.cubicTo(cp1x, cp1y, cp2x, cp2y, p2.x, p2.y)
    }
    return path
}