package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.WeatherReport
import com.alexser.weathernote.domain.model.WeatherType
import com.alexser.weathernote.utils.formatAsSpanishDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun WeatherCard(
    report: WeatherReport,
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
                    text = report.date.formatAsSpanishDate(),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
                Text(
                    text = "${report.temperature}°C",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            WeatherIcon(type = report.weatherType)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeatherCardPreview() {
    val report = WeatherReport(
        city = "Madrid",
        date = LocalDate.now(),
        temperature = 27.5f,
        weatherType = WeatherType.SUNNY
    )

    WeatherCard(report = report)
}

