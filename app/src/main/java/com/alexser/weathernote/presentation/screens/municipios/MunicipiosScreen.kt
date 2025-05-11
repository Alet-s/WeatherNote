package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
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
    showPrompt: Boolean = false // ✅ New parameter to trigger onboarding prompt
) {
    val municipios by viewModel.municipios.collectAsState()
    val snapshots by viewModel.snapshots.collectAsState()
    val fullForecasts by viewModel.fullForecasts.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    val selectedMunicipio = remember { mutableStateOf<SavedMunicipio?>(null) }
    var nameInput by remember { mutableStateOf(TextFieldValue()) }

    val currentHour = LocalTime.now().hour.toString().padStart(2, '0')
    val currentFullItem: HourlyForecastFullItem? =
        selectedMunicipio.value?.let { municipio ->
            fullForecasts[municipio.id]?.firstOrNull { it.hour == currentHour }
        }

    // ✅ Show onboarding snackbar prompt once
    LaunchedEffect(showPrompt) {
        if (showPrompt) {
            snackbarHostState.showSnackbar("Use the search bar to add a municipio.")
        }
    }

    // Automatically trigger forecast fetch
    LaunchedEffect(selectedMunicipio.value) {
        selectedMunicipio.value?.let {
            viewModel.fetchHourlyForecast(it.id)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Municipios") },
                actions = {
                    IconButton(onClick = { viewModel.reloadFromFirestore() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reload")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
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

            LazyColumn {
                items(municipios) { municipio ->
                    val snapshot = snapshots[municipio.id]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (snapshot != null) {
                                WeatherCard(
                                    report = snapshot,
                                    modifier = Modifier.clickable {
                                        selectedMunicipio.value = municipio
                                    },
                                    onSetHome = {
                                        viewModel.setHomeMunicipio(municipio.id)
                                    }
                                )
                            } else {
                                LinearProgressIndicator(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp)
                                )
                            }
                        }
                        IconButton(
                            onClick = { viewModel.removeMunicipio(municipio.id) },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete municipio")
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
        }
    }
}
