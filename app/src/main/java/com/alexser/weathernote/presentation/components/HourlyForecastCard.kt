package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.data.remote.model.HourlyForecastItem
import com.alexser.weathernote.domain.model.CondicionMeteorologica

/**
 * Composable que muestra una tarjeta compacta con la previsión meteorológica para una hora específica.
 *
 * La tarjeta incluye la hora, un icono representativo de la condición meteorológica,
 * y la temperatura prevista para esa hora.
 *
 * @param item Objeto [HourlyForecastItem] con la información meteorológica horaria.
 */
@Composable
fun HourlyForecastCard(item: HourlyForecastItem) {
    val condition = CondicionMeteorologica.fromDescripcion(item.condition)

    Card(
        modifier = Modifier
            .size(82.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp),
        border = BorderStroke(0.2.dp, Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = String.format("%02d:00", item.hour.toInt()),
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center
            )

            Icon(
                painter = painterResource(id = condition.iconoRes),
                contentDescription = condition.descripcion,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = "${item.temperature}°",
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}
