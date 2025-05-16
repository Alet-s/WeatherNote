package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexser.weathernote.presentation.components.HourlyForecastRow
import com.alexser.weathernote.utils.formatMunicipioName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipiosHorariaScreen(
    viewModel: MunicipiosHorariaScreenViewModel = hiltViewModel()
) {
    val municipios by viewModel.municipios.collectAsState()
    val hourlyForecasts by viewModel.hourlyForecasts.collectAsState()
    var selectedMunicipioId by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        viewModel.reloadForecasts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // TopAppBar replacement
        Text(
            text = selectedMunicipioId?.let { id ->
                municipios.find { it.id == id }?.let { formatMunicipioName(it.nombre) }
            } ?: "Predicción horaria",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (selectedMunicipioId == null) {
            Text(
                text = "Municipios guardados",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(municipios) { municipio ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedMunicipioId = municipio.id
                            }
                    ) {
                        Text(
                            text = formatMunicipioName(municipio.nombre),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        } else {
            val forecast = hourlyForecasts[selectedMunicipioId].orEmpty()

            Text(
                text = "Predicción por hora",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            if (forecast.isEmpty()) {
                Text("No hay datos disponibles.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(forecast) { item ->
                        HourlyForecastRow(forecast = item)
                        Divider()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { selectedMunicipioId = null },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver a la lista")
            }
        }
    }
}
