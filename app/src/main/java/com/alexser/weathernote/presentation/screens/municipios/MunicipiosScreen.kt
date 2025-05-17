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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
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
    val snapshotStates by viewModel.snapshotUiStates.collectAsState()
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
            snackbarHostState.showSnackbar("Use the search bar to add a municipio.")
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
            Text("Saved Municipios", style = MaterialTheme.typography.titleLarge)
            Row {
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                IconButton(onClick = { viewModel.reloadFromFirestore() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reload")
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
                        label = { Text("Municipio name") },
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
                        Text("Add")
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
                        is SnapshotUiState.Loading -> {
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            )
                        }
                        is SnapshotUiState.Success -> {
                            WeatherCard(
                                report = snapshotState.snapshot,
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
                        is SnapshotUiState.Error -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Error loading data: ${snapshotState.message}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error
                                )
                                TextButton(onClick = {
                                    viewModel.fetchSnapshot(municipio.id)
                                }) {
                                    Text("Retry")
                                }
                            }
                        }
                        null -> {
                            Text("No state")
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
                            Text("Close")
                        }
                    },
                    title = { Text("Loading forecast...") },
                    text = { Text("Please wait while we fetch the current hour's data.") }
                )
            }
        }

        municipioToDelete.value?.let { municipio ->
            AlertDialog(
                onDismissRequest = { municipioToDelete.value = null },
                title = { Text("Delete ${municipio.nombre}?") },
                text = {
                    Text("Do you also want to delete all snapshots for this municipio?")
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.removeMunicipioWithOption(municipio.id, deleteSnapshots = true)
                        municipioToDelete.value = null
                    }) {
                        Text("Delete with snapshots")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        viewModel.removeMunicipioWithOption(municipio.id, deleteSnapshots = false)
                        municipioToDelete.value = null
                    }) {
                        Text("Keep snapshots")
                    }
                }
            )
        }
    }
}
