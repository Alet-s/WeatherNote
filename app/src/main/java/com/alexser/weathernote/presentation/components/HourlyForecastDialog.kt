package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem

/**
 * Muestra un diálogo con la previsión meteorológica detallada para una hora específica.
 *
 * El diálogo presenta datos como temperatura, sensación térmica, condición meteorológica,
 * precipitación, nieve, humedad relativa, velocidad y dirección del viento, y racha máxima.
 *
 * @param data Datos meteorológicos detallados para la hora mostrada, en un objeto [HourlyForecastFullItem].
 * @param onDismiss Lambda que se ejecuta al cerrar el diálogo.
 */
@Composable
fun HourlyForecastDialog(
    data: HourlyForecastFullItem,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = { Text("${stringResource(R.string.tiempo_a_las)} ${data.hour}:00") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InfoRow(stringResource(R.string.temperatura), "${data.temperature ?: "--"}°C")
                InfoRow(stringResource(R.string.sensacion_termica), "${data.feelsLike ?: "--"}°C")
                InfoRow(stringResource(R.string.condicion_meteo), data.condition ?: "--")
                InfoRow(stringResource(R.string.precipitacion), "${data.precipitation ?: 0.0} mm")
                InfoRow(stringResource(R.string.nieve), "${data.snow ?: 0.0} mm")
                InfoRow(stringResource(R.string.humedad_relativa), "${data.humidity ?: "--"}%")
                InfoRow(
                    stringResource(R.string.velocidad_viento),
                    "${data.windSpeed ?: "--"} km/h ${data.windDirection ?: ""}"
                )
                InfoRow(stringResource(R.string.racha_max), "${data.maxGust ?: "--"} km/h")
            }
        }
    )
}

/**
 * Fila con etiqueta y valor para mostrar información en formato clave:valor.
 *
 * Se usa para mostrar pares de texto en el diálogo de previsión horaria.
 *
 * @param label Texto descriptivo o clave.
 * @param value Texto con el valor correspondiente.
 */
@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
