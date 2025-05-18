package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.material3.*
import androidx.compose.material3.R
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
import com.google.rpc.context.AttributeContext

@Composable
fun BigWeatherCard(
    report: BasicWeatherForecast,
    modifier: Modifier = Modifier,
    onEditClick: () -> Unit,
    onRemoveClick: () -> Unit
) {
    val condition = CondicionMeteorologica.fromDescripcion(report.condition)
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {

            // Action buttons (edit & delete) top right
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

            // Main content
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = formatMunicipioName(report.city),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = formatIsoDateAsSpanish(report.date),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Icon(
                    painter = painterResource(id = condition.iconoRes),
                    contentDescription = condition.descripcion,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(115.dp)
                        .padding(vertical = 8.dp)
                )

                // Temperature + Celsius icon
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = "${report.maxTemp}° / ${report.minTemp}°",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                Text(
                    text = condition.descripcion,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}
