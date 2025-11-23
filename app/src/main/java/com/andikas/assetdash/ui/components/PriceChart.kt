package com.andikas.assetdash.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.andikas.assetdash.domain.model.PricePoint
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

@Composable
fun PriceChart(
    dataPoints: List<PricePoint>,
    modifier: Modifier = Modifier
) {
    val chartEntryModelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(dataPoints) {
        val entries = dataPoints.mapIndexed { index, point ->
            entryOf(index.toFloat(), point.price)
        }
        chartEntryModelProducer.setEntries(entries)
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surface

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            primaryColor.copy(alpha = 0.4f),
            primaryColor.copy(alpha = 0.0f)
        )
    )

    Chart(
        chart = lineChart(
            lines = listOf(
                LineChart.LineSpec(
                    lineColor = primaryColor.toArgb(),
                    lineThicknessDp = 3f,
                    pointConnector = DefaultPointConnector(
                        cubicStrength = 0.25f
                    ),
                    lineBackgroundShader = DynamicShaders.fromBrush(gradientBrush),
                )
            ),
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = if (dataPoints.isNotEmpty()) dataPoints.minOf { it.price } * 0.99f else 0f,
                maxY = if (dataPoints.isNotEmpty()) dataPoints.maxOf { it.price } * 1.01f else 0f
            ),
        ),
        chartModelProducer = chartEntryModelProducer,
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp),
        isZoomEnabled = false
    )
}