package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.components.HourlyForecastDialog
import com.alexser.weathernote.presentation.components.WeatherCard
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipiosScreen(
    viewModel: MunicipiosScreenViewModel,
    showPrompt: Boolean = false
) {
    val municipios by viewModel.municipios.collectAsState()
    val snapshotStates by viewModel.snapshotMunicipioUiStates.collectAsState()
    val fullForecasts by viewModel.hourlyFullForecasts.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val homeMunicipioId by viewModel.homeMunicipioId.collectAsState(initial = null)
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()
    val homeConfirmation by viewModel.homeConfirmationMessage.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val selectedMunicipio = remember { mutableStateOf<SavedMunicipio?>(null) }
    val municipioToDelete = remember { mutableStateOf<SavedMunicipio?>(null) }

    var nameInput by remember { mutableStateOf(TextFieldValue()) }
    var showSearch by remember { mutableStateOf(false) }
    val promptTextAddMuni = stringResource(R.string.usa_barra_anyadir_muni)


    val currentHour = LocalTime.now().hour.toString().padStart(2, '0')
    val currentFullItem: HourlyForecastFullItem? =
        selectedMunicipio.value?.let { municipio ->
            fullForecasts[municipio.id]?.firstOrNull { it.hour == currentHour }
        }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    LaunchedEffect(homeConfirmation) {
        homeConfirmation?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearHomeConfirmationMessage()
        }
    }

    LaunchedEffect(showPrompt) {
        if (showPrompt) {
            snackbarHostState.showSnackbar(promptTextAddMuni)
        }
    }

    LaunchedEffect(selectedMunicipio.value) {
        selectedMunicipio.value?.let {
            viewModel.fetchHourlyForecast(it.id)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.munis_guardados), style = MaterialTheme.typography.titleLarge)
            Row {
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.buscar))
                }
                IconButton(onClick = { viewModel.reloadFromFirestore() }) {
                    Icon(Icons.Default.Refresh, contentDescription = stringResource(R.string.recargar))
                }
            }
        }

        SnackbarHost(hostState = snackbarHostState, modifier = Modifier.fillMaxWidth())

        AnimatedVisibility(visible = showSearch) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text(stringResource(R.string.nombre_muni)) },
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = {
                        if (nameInput.text.isNotBlank()) {
                            viewModel.addMunicipioByName(nameInput.text)
                            nameInput = TextFieldValue()
                            showSearch = false
                        }
                    }) {
                        Text(stringResource(R.string.anyadir))
                    }
                }

                suggestions.forEach { suggestion ->
                    Text(
                        text = suggestion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .clickable {
                                nameInput = TextFieldValue(suggestion)
                            }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        LazyColumn {
            items(municipios) { municipio ->
                val snapshotState = snapshotStates[municipio.id]

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    when (snapshotState) {
                        is SnapshotMunicipioUiState.Loading -> {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                        is SnapshotMunicipioUiState.Success -> {
                            WeatherCard(
                                report = snapshotState.basicWeatherForecast,
                                modifier = Modifier.clickable {
                                    selectedMunicipio.value = municipio
                                },
                                onSetHome = if (municipio.id != homeMunicipioId) {
                                    { viewModel.setHomeMunicipio(municipio.id) }
                                } else null,
                                onDelete = {
                                    municipioToDelete.value = municipio
                                }
                            )
                        }
                        is SnapshotMunicipioUiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${stringResource(R.string.error_cargar_datos)} ${snapshotState.message}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                                TextButton(onClick = {
                                    viewModel.fetchSnapshot(municipio.id)
                                }) {
                                    Text(stringResource(R.string.reintentar))
                                }
                            }
                        }
                        null -> {
                            Text(stringResource(R.string.sin_estado))
                        }
                    }
                }
            }
        }

        selectedMunicipio.value?.let {
            if (currentFullItem != null) {
                HourlyForecastDialog(
                    data = currentFullItem,
                    onDismiss = { selectedMunicipio.value = null }
                )
            } else {
                AlertDialog(
                    onDismissRequest = { selectedMunicipio.value = null },
                    confirmButton = {
                        TextButton(onClick = { selectedMunicipio.value = null }) {
                            Text(stringResource(R.string.cerrar))
                        }
                    },
                    title = { Text(stringResource(R.string.cargando_prediccion)) },
                    text = { Text(stringResource(R.string.porfavor_espera_prediccion)) }
                )
            }
        }

        municipioToDelete.value?.let { municipio ->
            AlertDialog(
                onDismissRequest = { municipioToDelete.value = null },
                title = { Text("${stringResource(R.string.eliminar)} ${municipio.nombre}?") },
                text = {
                    Text(stringResource(R.string.borrar_tambien_snaps))
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.removeMunicipioWithOption(municipio.id, deleteSnapshots = true)
                        municipioToDelete.value = null
                    }) {
                        Text(stringResource(R.string.borrar_con_snaps))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.removeMunicipioWithOption(municipio.id, deleteSnapshots = false)
                        municipioToDelete.value = null
                    }) {
                        Text(stringResource(R.string.mantener_snapshots))
                    }
                }
            )
        }
    }
}
