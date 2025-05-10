package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem

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
        title = { Text("Weather at ${data.hour}:00") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                InfoRow("Temperature", "${data.temperature ?: "--"}°C")
                InfoRow("Feels like", "${data.feelsLike ?: "--"}°C")
                InfoRow("Condition", data.condition ?: "--")
                InfoRow("Precipitation", "${data.precipitation ?: 0.0} mm")
                InfoRow("Snow", "${data.snow ?: 0.0} mm")
                InfoRow("Humidity", "${data.humidity ?: "--"}%")
                InfoRow("Wind", "${data.windSpeed ?: "--"} km/h ${data.windDirection ?: ""}")
                InfoRow("Max gust", "${data.maxGust ?: "--"} km/h")
            }
        }
    )
}

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
