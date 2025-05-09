package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.data.remote.model.HourlyForecastItem

@Composable
fun HourlyForecastRow(
    forecast: HourlyForecastItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "${forecast.hour}:00",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )
        Text(
            text = "${forecast.temperature}°C",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center
        )
        Text(
            text = forecast.condition,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}
