package com.alexser.weathernote.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.presentation.components.BigWeatherCard
import com.alexser.weathernote.presentation.components.HourlyForecastCard
import com.alexser.weathernote.presentation.components.HourlyForecastDialog
import com.alexser.weathernote.presentation.screens.home.SnapshotHomeUiState
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import java.time.LocalTime
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel,
    onLogout: () -> Unit,
    onRequestAddFavorite: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val selectedHourForecast = remember { mutableStateOf<HourlyForecastFullItem?>(null) }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.p_principal), style = MaterialTheme.typography.titleLarge)
            TextButton(onClick = {
                viewModel.logout()
                onLogout()
            }) {
                Text(stringResource(R.string.cerrar_sesion))
            }
        }

        when (uiState) {
            is SnapshotHomeUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is SnapshotHomeUiState.Success -> {
                val report = (uiState as SnapshotHomeUiState.Success).data
                val hourly = (uiState as SnapshotHomeUiState.Success).hourly
                val hourlyFull = (uiState as SnapshotHomeUiState.Success).hourlyFull

                val currentHour = LocalTime.now().hour.toString().padStart(2, '0')
                val currentItem = hourlyFull?.firstOrNull { it.hour == currentHour }

                Text(
                    stringResource(R.string.tiempo_hoy),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(modifier = Modifier.clickable {
                    if (currentItem != null) {
                        selectedHourForecast.value = currentItem
                    }
                }) {
                    BigWeatherCard(
                        report = report,
                        onEditClick = onRequestAddFavorite,
                        onRemoveClick = viewModel::clearHomeMunicipio
                    )
                }

                if (hourly.isNotEmpty()) {
                    Text(
                        text = stringResource(R.string.prox_horas),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(hourly) { item ->
                            HourlyForecastCard(item)
                        }
                    }
                }

                Button(
                    onClick = { viewModel.generateSnapshotManually() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                ) {
                    Text(stringResource(R.string.generar_snap))
                }
            }

            is SnapshotHomeUiState.Error -> {
                val message = (uiState as SnapshotHomeUiState.Error).message
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Error: $message",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 32.dp)
                    )
                }
            }

            is SnapshotHomeUiState.Idle -> {
                Text(
                    text = stringResource(R.string.busca_ver_tiempo),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }
        }

        selectedHourForecast.value?.let { item ->
            HourlyForecastDialog(
                data = item,
                onDismiss = { selectedHourForecast.value = null }
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
