package com.alexser.weathernote.presentation.components

import android.content.Context
import android.widget.TextView
import com.alexser.weathernote.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.charts.LineChart

/**
 * Vista personalizada que muestra un marcador sobre el gráfico de líneas
 * para mostrar información detallada sobre un punto específico del snapshot.
 *
 * @property context Contexto de la aplicación
 * @property snapshotMap Mapa que asocia valores X con etiquetas descriptivas
 */
class SnapshotMarkerView(
    context: Context,
    snapshotMap: Map<Float, String> = emptyMap()
) : MarkerView(context, R.layout.marker_view) {

    /** Referencia al gráfico de líneas asociado */
    lateinit var chartView: LineChart

    /** TextView donde se muestra el texto del marcador */
    private val textView: TextView = findViewById(R.id.marker_text)

    /** Mapa que relaciona la posición X con etiquetas descriptivas */
    internal var labelMap: Map<Float, String> = snapshotMap

    /**
     * Actualiza el contenido del marcador cada vez que se resalta un punto en el gráfico.
     *
     * @param e Entrada del gráfico que representa el punto seleccionado
     * @param highlight Información del punto resaltado
     */
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

    /**
     * Define el desplazamiento del marcador respecto al punto seleccionado
     * para que quede centrado horizontalmente y arriba del punto.
     *
     * @return Punto que indica el desplazamiento del marcador
     */
    override fun getOffset(): MPPointF {
        return MPPointF(-(width / 2f), -height.toFloat())
    }
}
