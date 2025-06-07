package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable que muestra una lista de checkboxes organizados en filas de dos elementos.
 *
 * @param selected Un [MutableMap] que representa los ítems con sus etiquetas como clave y el estado seleccionado (true/false) como valor.
 *                 Al cambiar el estado de un checkbox, se actualiza el valor correspondiente en este mapa.
 *
 * El componente organiza los ítems en filas, con hasta dos checkboxes por fila,
 * mostrando la etiqueta junto al checkbox correspondiente.
 */
@Composable
fun MetricsCheckboxes(selected: MutableMap<String, Boolean>) {
    val items = selected.entries.toList().chunked(2)

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items.forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { (label, isChecked) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { selected[label] = it }
                        )
                        Text(label)
                    }
                }

                if (rowItems.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}
