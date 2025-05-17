package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName

@Composable
fun WeatherCard(
    report: BasicWeatherForecast,
    modifier: Modifier = Modifier,
    onSetHome: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                Text("Max: ${report.maxTemp}°C / Min: ${report.minTemp}°C")
                Text(report.condition)

                Spacer(modifier = Modifier.height(8.dp))

                if (onSetHome != null || onDelete != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.End
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
            }
        }
    }
}
