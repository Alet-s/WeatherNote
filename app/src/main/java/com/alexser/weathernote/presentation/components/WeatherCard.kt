package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.model.CondicionMeteorologica
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName

@Composable
fun WeatherCard(
    report: BasicWeatherForecast,
    modifier: Modifier = Modifier,
    onSetHome: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val condition = CondicionMeteorologica.fromDescripcion(report.condition)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Action icons at top right
            if (onSetHome != null || onDelete != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (onSetHome != null) {
                        IconButton(onClick = onSetHome) {
                            Icon(Icons.Default.Home, contentDescription = "Set as Home")
                        }
                    }
                    if (onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }

            // Weather content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formatMunicipioName(report.city),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatIsoDateAsSpanish(report.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    painter = painterResource(id = condition.iconoRes),
                    contentDescription = condition.descripcion,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Max: ${report.maxTemp}°C / Min: ${report.minTemp}°C")
                Text(condition.descripcion)
            }
        }
    }
}

