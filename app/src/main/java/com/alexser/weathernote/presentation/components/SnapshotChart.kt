package com.alexser.weathernote.presentation.components

import android.graphics.Color
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.alexser.weathernote.domain.model.SnapshotReport
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter

@Composable
fun SnapshotChart(
    snapshots: List<SnapshotReport>,
    selected: Map<String, Boolean>
) {
    val context = LocalContext.current

    val metricColors = mapOf(
        "Temperature" to Color.RED,
        "Humidity" to Color.BLUE,
        "Precipitation" to Color.CYAN,
        "Wind Speed" to Color.GREEN
    )

    val dataSets = selected.entries.filter { it.value }.mapNotNull { (metric, _) ->
        val entries = snapshots.mapIndexedNotNull { index, snapshot ->
            val value = when (metric) {
                "Temperature" -> snapshot.temperature?.toFloat()
                "Humidity" -> snapshot.humidity?.toFloat()
                "Precipitation" -> snapshot.precipitation?.toFloat()
                "Wind Speed" -> snapshot.windSpeed?.toFloat()
                else -> null
            }
            value?.let { Entry(index.toFloat(), it) }
        }

        if (entries.isNotEmpty()) {
            LineDataSet(entries, metric).apply {
                color = metricColors[metric] ?: Color.BLACK
                setCircleColor(color)
                lineWidth = 2f
                circleRadius = 3f
                setDrawValues(false)
            }
        } else null
    }

    AndroidView(
        factory = {
            val chart = LineChart(context)
            val labelMap = snapshots.mapIndexed { index, it ->
                index.toFloat() to it.timestamp.substringBefore("T")
            }.toMap()

            chart.apply {
                description = Description().apply {
                    text = "Evolución meteorológica"
                    textSize = 12f
                }
                axisRight.isEnabled = false
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)
                xAxis.labelRotationAngle = -45f
                xAxis.textSize = 10f
                legend.isEnabled = true
                animateX(500)

                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return labelMap[value] ?: ""
                    }
                }

                val marker = SnapshotMarkerView(context, labelMap)
                marker.chartView = this
                this.marker = marker
            }
        },
        update = { chart ->
            chart.highlightValue(null) // 🔒 Clear any active marker
            chart.marker = null        // 🔒 Detach old marker

            val labelMap = snapshots.mapIndexed { index, it ->
                index.toFloat() to it.timestamp.substringBefore("T")
            }.toMap()

            chart.data = LineData(dataSets)

            val newMarker = SnapshotMarkerView(chart.context, labelMap)
            newMarker.chartView = chart
            chart.marker = newMarker

            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
