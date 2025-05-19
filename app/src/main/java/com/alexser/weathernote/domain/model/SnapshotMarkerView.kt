package com.alexser.weathernote.presentation.components

import android.content.Context
import android.widget.TextView
import com.alexser.weathernote.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.charts.LineChart
import com.alexser.weathernote.domain.model.SnapshotReport

class SnapshotMarkerView(
    context: Context,
    snapshotMap: Map<Float, String> = emptyMap()
) : MarkerView(context, R.layout.marker_view) {

    lateinit var chartView: LineChart
    private val textView: TextView = findViewById(R.id.marker_text)

    internal var labelMap: Map<Float, String> = snapshotMap

    fun updateLabelsFromSnapshots(snapshots: List<SnapshotReport>) {
        labelMap = snapshots.mapIndexed { index, snapshot ->
            index.toFloat() to snapshot.timestamp.substringBefore("T")
        }.toMap()
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        try {
            val label = e?.x?.let { labelMap[it] }
            val value = e?.y?.toInt() ?: return
            val metric = highlight?.dataSetIndex?.let { index ->
                chartView.data.getDataSetByIndex(index)?.label
            }

            val unit = when (metric) {
                "Temperature" -> "°C"
                "Humidity" -> "%"
                "Precipitation" -> "mm"
                "Wind Speed" -> "km/h"
                else -> ""
            }

            val text = if (label != null) "$value$unit • $label" else "$value$unit"
            textView.text = text
            super.refreshContent(e, highlight)
        } catch (_: Exception) {
            textView.text = "?"
        }
    }


    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
