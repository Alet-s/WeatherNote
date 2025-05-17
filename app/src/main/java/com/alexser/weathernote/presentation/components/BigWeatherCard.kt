package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName // <-- Import added

@Composable
fun BigWeatherCard(
    report: BasicWeatherForecast,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formatMunicipioName(report.city), // <-- Applied formatting here
                    style = MaterialTheme.typography.headlineSmall
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

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Outlined.Edit, contentDescription = "Edit Home Municipio")
                }
                IconButton(onClick = onRemoveClick) {
                    Icon(Icons.Outlined.DeleteOutline, contentDescription = "Remove Home Municipio")
                }
            }
        }
    }
}
