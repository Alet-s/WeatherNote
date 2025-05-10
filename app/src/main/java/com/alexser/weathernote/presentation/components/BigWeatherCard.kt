package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.Snapshot
import com.alexser.weathernote.utils.formatIsoDateAsSpanish

@Composable
fun BigWeatherCard(
    report: Snapshot,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = report.city,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = formatIsoDateAsSpanish(report.date),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "${report.maxTemp}°C / ${report.minTemp}°C",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Text(
                text = report.condition,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}
