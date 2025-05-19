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

    val metrics = mapOf(
        "Temperature" to Color.RED,
        "Humidity" to Color.BLUE,
        "Precipitation" to Color.CYAN,
        "Wind Speed" to Color.GREEN
    )

    val selectedMetrics = selected.filter { it.value }.keys
    if (selectedMetrics.isEmpty()) return

    val dates = snapshots.map { it.timestamp.substringBefore("T") }

    AndroidView(
        factory = {
            LineChart(context).apply {
                val dataSets = selectedMetrics.mapNotNull { metric ->
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
                            color = metrics[metric] ?: Color.BLACK
                            setCircleColor(color)
                            lineWidth = 2f
                            circleRadius = 4f
                            valueTextSize = 10f
                            setDrawValues(false)
                        }
                    } else null
                }

                data = LineData(dataSets)

                // Description
                description = Description().apply {
                    text = "Selected metrics over time"
                    textSize = 12f
                }

                // X-Axis: use dates
                xAxis.granularity = 1f
                xAxis.setDrawGridLines(false)
                xAxis.valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        val index = value.toInt()
                        return dates.getOrNull(index) ?: ""
                    }
                }

                axisRight.isEnabled = false
                legend.isEnabled = true
                animateX(500)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    )
}
