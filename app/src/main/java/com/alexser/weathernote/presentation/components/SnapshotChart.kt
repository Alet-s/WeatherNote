package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData


@Composable
fun SnapshotChart(
    snapshots: List<SnapshotReport>,
    selected: Map<String, Boolean>
) {
    val dateLabels = snapshots.map { it.timestamp.substringBefore("T") }

    val lines = mutableListOf<LineChartData>()

    if (selected["Temperature"] == true) {
        lines += LineChartData(
            points = snapshots.mapIndexedNotNull { index, s ->
                s.temperature?.toFloat()?.let { LineChartData.Point(it, index.toFloat()) }
            },
            lineColor = MaterialTheme.colorScheme.primary
        )
    }
    if (selected["Humidity"] == true) {
        lines += LineChartData(
            points = snapshots.mapIndexedNotNull { index, s ->
                s.humidity?.toFloat()?.let { LineChartData.Point(it, index.toFloat()) }
            },
            lineColor = MaterialTheme.colorScheme.secondary
        )
    }
    if (selected["Precipitation"] == true) {
        lines += LineChartData(
            points = snapshots.mapIndexedNotNull { index, s ->
                s.precipitation?.toFloat()?.let { LineChartData.Point(it, index.toFloat()) }
            },
            lineColor = MaterialTheme.colorScheme.tertiary
        )
    }
    if (selected["Wind Speed"] == true) {
        lines += LineChartData(
            points = snapshots.mapIndexedNotNull { index, s ->
                s.windSpeed?.toFloat()?.let { LineChartData.Point(it, index.toFloat()) }
            },
            lineColor = MaterialTheme.colorScheme.error
        )
    }

    if (lines.isNotEmpty()) {
        LineChart(
            lines = lines,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    } else {
        Text("Selecciona al menos un parámetro para visualizar el gráfico.")
    }
}
