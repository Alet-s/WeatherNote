package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.SnapshotReport

/**
 * Diálogo que muestra los detalles completos de un reporte meteorológico (SnapshotReport).
 *
 * Muestra información como municipio, fecha y hora, estado del tiempo, temperatura,
 * sensación térmica, humedad, precipitación, nieve, viento, racha máxima y dirección del viento.
 * También muestra una nota del usuario si está disponible.
 *
 * @param snapshot Reporte meteorológico cuyos detalles se mostrarán.
 * @param onDismiss Lambda que se invoca para cerrar el diálogo.
 */
@Composable
fun SnapshotDetailDialog(
    snapshot: SnapshotReport,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        },
        title = { Text("Snapshot Details") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Municipio: ${snapshot.municipioName}")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccessTime, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Fecha/Hora: ${snapshot.timestamp}")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WbCloudy, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Estado: ${snapshot.condition ?: "-"}")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Thermostat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Temperatura: ${snapshot.temperature ?: "-"}°C")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DeviceThermostat, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sensación térmica: ${snapshot.feelsLike ?: "-"}°C")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.WaterDrop, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Humedad: ${snapshot.humidity ?: "-"}%")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Grain, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Precipitación: ${snapshot.precipitation ?: "-"} mm")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AcUnit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nieve: ${snapshot.snow ?: "-"} mm")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Air, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Viento: ${snapshot.windSpeed ?: "-"} km/h")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Speed, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Racha máx.: ${snapshot.maxGust ?: "-"} km/h")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Explore, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dirección viento: ${snapshot.windDirection ?: "-"}")
                }
                if (!snapshot.userNote.isNullOrBlank()) {
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.EditNote, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Nota: ${snapshot.userNote}")
                    }
                }
            }
        }
    )
}
