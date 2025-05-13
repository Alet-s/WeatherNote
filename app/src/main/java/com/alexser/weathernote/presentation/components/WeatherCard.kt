package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName // ✅ Import the formatter
import java.time.LocalDate

@Composable
fun WeatherCard(
    report: Snapshot,
    modifier: Modifier = Modifier,
    onSetHome: (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = formatMunicipioName(report.city), // ✅ Format the city name
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatIsoDateAsSpanish(report.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text("Max: ${report.maxTemp}°C / Min: ${report.minTemp}°C")
                Text(report.condition)
            }

            if (onSetHome != null) {
                IconButton(onClick = onSetHome) {
                    Icon(Icons.Default.Home, contentDescription = "Set as Home")
                }
            }
        }
    }
}
