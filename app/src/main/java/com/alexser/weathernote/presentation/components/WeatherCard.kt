package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.model.CondicionMeteorologica
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName

/**
 * Componente que muestra una tarjeta con información meteorológica básica para un municipio y fecha concretos.
 *
 * Muestra el nombre del municipio, fecha formateada, icono de condición meteorológica, temperaturas máxima y mínima,
 * y descripción del estado del cielo.
 *
 * Además, permite opcionalmente establecer el municipio como principal y eliminarlo, mostrando botones para estas acciones.
 *
 * @param report Objeto [BasicWeatherForecast] con los datos a mostrar.
 * @param modifier Modificador para personalizar el layout.
 * @param onSetHome Callback opcional para marcar el municipio como principal. Si es null, no se muestra el botón.
 * @param onDelete Callback opcional para eliminar el municipio. Si es null, no se muestra el botón.
 */
@Composable
fun WeatherCard(
    report: BasicWeatherForecast,
    modifier: Modifier = Modifier,
    onSetHome: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null
) {
    val condition = CondicionMeteorologica.fromDescripcion(report.condition)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (onSetHome != null || onDelete != null) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (onSetHome != null) {
                        IconButton(onClick = onSetHome) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = stringResource(R.string.config_como_principal)
                            )
                        }
                    }
                    if (onDelete != null) {
                        IconButton(onClick = onDelete) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.eliminar)
                            )
                        }
                    }
                }
            }

            // Weather content
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = formatMunicipioName(report.city),
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatIsoDateAsSpanish(report.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                Icon(
                    painter = painterResource(id = condition.iconoRes),
                    contentDescription = condition.descripcion,
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(80.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Max: ${report.maxTemp}°C / Min: ${report.minTemp}°C")
                Text(condition.descripcion)
            }
        }
    }
}

