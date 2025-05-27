package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.domain.model.CondicionMeteorologica
import com.alexser.weathernote.domain.model.DailyForecast
import com.alexser.weathernote.utils.formatDateAsDayAndMonth
import androidx.compose.ui.res.painterResource


@Composable
fun DailyForecastCard(
    forecast: DailyForecast,
    modifier: Modifier = Modifier
) {
    val condition = CondicionMeteorologica.fromDescripcion(forecast.condition)

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        modifier = modifier
            .padding(4.dp)
            .width(120.dp)
            .height(160.dp) // Altura fija para uniformidad
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatDateAsDayAndMonth(forecast.date),
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
            Icon(
                painter = painterResource(id = condition.iconoRes),
                contentDescription = forecast.condition,
                modifier = Modifier
                    .size(65.dp)
                    .padding(vertical = 4.dp)
            )
            Text(
                text = forecast.condition,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
            Text(
                text = "↑ ${forecast.maxTemp}° ↓ ${forecast.minTemp}°",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}