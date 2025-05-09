package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexser.weathernote.presentation.components.HourlyForecastRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipiosHorariaScreen(
    viewModel: MunicipiosHorariaScreenViewModel = hiltViewModel()
) {
    val municipios by viewModel.municipios.collectAsState()
    val hourlyForecasts by viewModel.hourlyForecasts.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Hourly Forecasts") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            municipios.forEach { municipio ->
                item {
                    Text(
                        text = municipio.nombre,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(hourlyForecasts[municipio.id].orEmpty()) { item ->
                    HourlyForecastRow(forecast = item)
                    Divider()
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
