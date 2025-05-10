package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import java.time.LocalDate

@Composable
fun WeatherCard(
    report: Snapshot,
    modifier: Modifier = Modifier
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
                Text(report.city, style = MaterialTheme.typography.titleMedium)

                Text(
                    text = formatIsoDateAsSpanish(report.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Text(
                    text = "Max: ${report.maxTemp}°C / Min: ${report.minTemp}°C",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = report.condition,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // Optionally: Show icon by condition string
            // WeatherIcon(type = mapConditionToType(report.condition))
        }
    }

}

//@Preview(showBackground = true)
//@Composable
//fun WeatherCardPreview() {
//    val report = Snapshot(
//        city = "Plasencia",
//        date = "2025-05-08T00:00:00",
//        maxTemp = 22.0f,
//        minTemp = 12.0f,
//        condition = "Muy nubo"
//    )
//    WeatherCard(report = report)
//}

