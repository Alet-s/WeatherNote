package com.alexser.weathernote.presentation.components

// .kt
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.data.remote.model.HourlyForecastItem

@Composable
fun HourlyForecastCard(item: HourlyForecastItem) {
    Card(
        modifier = Modifier
            .width(80.dp)
            .padding(horizontal = 4.dp, vertical = 8.dp),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = item.hour, style = MaterialTheme.typography.labelMedium)
            Text(text = "${item.temperature}°", style = MaterialTheme.typography.titleSmall)
            Text(text = item.condition, style = MaterialTheme.typography.bodySmall)
        }
    }
}
