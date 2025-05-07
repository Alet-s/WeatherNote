package com.alexser.weathernote.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.alexser.weathernote.domain.model.WeatherType

@Composable
fun WeatherIcon(type: WeatherType, modifier: Modifier = Modifier) {
    val emoji = when (type) {
        WeatherType.SUNNY -> "☀️"
        WeatherType.CLOUDY -> "☁️"
        WeatherType.RAINY -> "🌧️"
        WeatherType.STORMY -> "⛈️"
        WeatherType.SNOWY -> "❄️"
    }

    Text(
        text = emoji,
        style = MaterialTheme.typography.displaySmall,
        modifier = modifier
    )
}
