package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Componente que permite seleccionar un rango continuo dentro de un total de elementos,
 * mostrando etiquetas con fechas correspondientes a los extremos seleccionados.
 *
 * Este selector se basa en un [RangeSlider] que permite elegir un rango dentro de 0 hasta totalCount - 1.
 *
 * @param totalCount Número total de elementos entre los cuales seleccionar el rango.
 * @param selectedRange Rango actualmente seleccionado, en valores flotantes.
 * @param onRangeChange Callback que se invoca con el nuevo rango cuando el usuario lo modifica.
 * @param getDateLabel Función que recibe un índice entero y devuelve una etiqueta de fecha a mostrar.
 */
@Composable
fun SnapshotRangeSelector(
    totalCount: Int,
    selectedRange: ClosedFloatingPointRange<Float>,
    onRangeChange: (ClosedFloatingPointRange<Float>) -> Unit,
    getDateLabel: (Int) -> String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(getDateLabel(selectedRange.start.toInt()))
            Text(getDateLabel(selectedRange.endInclusive.toInt()))
        }

        Spacer(modifier = Modifier.height(8.dp))

        RangeSlider(
            value = selectedRange,
            onValueChange = onRangeChange,
            valueRange = 0f..(totalCount - 1).toFloat(),
            steps = if (totalCount > 2) totalCount - 1 else 0,
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        )
    }
}
