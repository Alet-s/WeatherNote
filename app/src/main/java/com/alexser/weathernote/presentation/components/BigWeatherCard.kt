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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.domain.model.BasicWeatherForecast
import com.alexser.weathernote.domain.model.CondicionMeteorologica
import com.alexser.weathernote.utils.formatIsoDateAsSpanish
import com.alexser.weathernote.utils.formatMunicipioName

/**
 * Composable que muestra una tarjeta grande con la predicción meteorológica básica de un municipio.
 *
 * Muestra el nombre del municipio, la fecha de la predicción, el icono de la condición meteorológica,
 * las temperaturas máxima y mínima, y la descripción del estado del cielo.
 * Además incluye botones para editar y eliminar el municipio principal.
 *
 * @param report Informe meteorológico básico que contiene los datos a mostrar.
 * @param modifier Modifier opcional para personalizar el layout.
 * @param onEditClick Lambda que se ejecuta al pulsar el botón de editar.
 * @param onRemoveClick Lambda que se ejecuta al pulsar el botón de eliminar.
 */
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

            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Outlined.Edit,
                        contentDescription = stringResource(R.string.editar_municipio_principal)
                    )
                }
                IconButton(onClick = onRemoveClick) {
                    Icon(
                        Icons.Outlined.DeleteOutline,
                        contentDescription = stringResource(R.string.quitar_municipio_principal)
                    )
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
