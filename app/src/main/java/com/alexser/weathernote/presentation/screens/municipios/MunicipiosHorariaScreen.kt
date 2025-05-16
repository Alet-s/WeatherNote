package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = selectedMunicipioId?.let { id ->
                            municipios.find { it.id == id }?.let { formatMunicipioName(it.nombre) }
                        } ?: "Predicción horaria"
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectedMunicipioId == null) {
                Text(
                    text = "Selecciona un municipio para ver su predicción horaria",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    municipios.forEach { municipio ->
                        ElevatedButton(
                            onClick = { selectedMunicipioId = municipio.id },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.elevatedButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(formatMunicipioName(municipio.nombre))
                        }
                    }
                }
            } else {
                val forecastList = hourlyForecasts[selectedMunicipioId].orEmpty()

                Text(
                    text = "Predicción por hora",
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    if (forecastList.isEmpty()) {
                        Text(
                            "No hay datos disponibles.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.padding(16.dp)
                        ) {
                            items(forecastList) { item ->
                                HourlyForecastRow(forecast = item)
                                Divider()
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { selectedMunicipioId = null },
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                ) {
                    Text("Volver a la lista")
                }
            }
        }
    }
}

