package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem

@Composable
fun CurrentHourWeatherCard(
    data: HourlyForecastFullItem,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = "${stringResource(R.string.tiempo_a_las)} ${data.hour}:00",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            InfoRow(stringResource(R.string.temperatura), "${data.temperature ?: "--"}°C")
            InfoRow(stringResource(R.string.sensacion_termica), "${data.feelsLike ?: "--"}°C")
            InfoRow(stringResource(R.string.condicion_meteo), data.condition ?: "--")
            InfoRow(stringResource(R.string.precipitacion), "${data.precipitation ?: 0.0} mm")
            InfoRow(stringResource(R.string.nieve), "${data.snow ?: 0.0} mm")
            InfoRow(stringResource(R.string.humedad_relativa), "${data.humidity ?: "--"}%")
            InfoRow(stringResource(R.string.velocidad_viento), "${data.windSpeed ?: "--"} km/h ${data.windDirection ?: ""}")
            InfoRow(stringResource(R.string.racha_max), "${data.maxGust ?: "--"} km/h")
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
