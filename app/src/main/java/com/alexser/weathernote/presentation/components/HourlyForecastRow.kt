package com.alexser.weathernote.presentation.components

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
 * Composable que muestra una fila con la previsión meteorológica para una hora específica.
 *
 * La fila incluye la hora, la temperatura, un icono representativo de la condición meteorológica
 * y una descripción textual de dicha condición.
 *
 * @param forecast Datos meteorológicos horarios, representados por un objeto [HourlyForecastItem].
 * @param modifier Modifier opcional para personalizar el layout del componente.
 */
@Composable
fun HourlyForecastRow(
    forecast: HourlyForecastItem,
    modifier: Modifier = Modifier
) {
    val condition = CondicionMeteorologica.fromDescripcion(forecast.condition)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
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

        Icon(
            painter = painterResource(id = condition.iconoRes),
            contentDescription = condition.descripcion,
            tint = Color.Unspecified,
            modifier = Modifier
                .size(24.dp)
                .weight(0.5f)
        )

        Text(
            text = condition.descripcion,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.End
        )
    }
}
