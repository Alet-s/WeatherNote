@file:OptIn(ExperimentalMaterial3Api::class)

package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R
import com.alexser.weathernote.data.remote.model.HourlyForecastFullItem
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.components.DailyForecastCard
import com.alexser.weathernote.presentation.components.HourlyForecastDialog
import com.alexser.weathernote.presentation.components.WeatherCard
import java.time.LocalTime

/**
 * Pantalla principal para mostrar la lista de municipios guardados por el usuario.
 *
 * Esta pantalla permite:
 * - Visualizar las tarjetas con el pronóstico básico del municipio.
 * - Expandir la tarjeta para mostrar un pronóstico diario detallado.
 * - Añadir nuevos municipios por búsqueda.
 * - Recargar datos desde Firestore.
 * - Mostrar diálogos con el pronóstico horario detallado.
 * - Gestionar municipios favoritos (home).
 * - Eliminar municipios con opción de borrar también sus snapshots meteorológicos.
 *
 * @param viewModel Instancia del ViewModel que gestiona el estado y la lógica de la pantalla.
 * @param showPrompt Indica si se debe mostrar un mensaje de ayuda para añadir municipios (por ejemplo, al entrar la primera vez).
 */
@Composable
fun MunicipiosScreen(
    viewModel: MunicipiosScreenViewModel,
    showPrompt: Boolean = false
) {

    /**
     * Lista de municipios guardados, obtenida del ViewModel como flujo y observada en Compose.
     * Contiene los municipios que el usuario ha añadido y está gestionando.
     */
    val municipios by viewModel.municipios.collectAsState()

    /**
     * Mapa que contiene el estado UI de la carga de snapshots meteorológicos por municipio.
     * Puede estar en estado Loading, Success (con datos) o Error (con mensaje).
     */
    val snapshotStates by viewModel.snapshotMunicipioUiStates.collectAsState()

    /**
     * Mapa que almacena los pronósticos horarios completos para cada municipio.
     * Se utiliza para mostrar información detallada en el diálogo de pronóstico horario.
     */
    val fullForecasts by viewModel.hourlyFullForecasts.collectAsState()

    /**
     * Mapa con el pronóstico diario para cada municipio, utilizado para mostrar la fila expandida en la tarjeta.
     */
    val dailyForecasts by viewModel.dailyForecasts.collectAsState()

    /**
     * Lista de sugerencias para autocompletar la búsqueda de municipios.
     * Actualizada según la entrada del usuario para facilitar la selección.
     */
    val suggestions by viewModel.suggestions.collectAsState()

    /**
     * ID del municipio marcado como "home" o favorito, mostrado con un icono especial.
     * Puede ser null si no hay ninguno seleccionado.
     */
    val homeMunicipioId by viewModel.homeMunicipioId.collectAsState(initial = null)

    /**
     * Mensaje de Snackbar para mostrar errores o confirmaciones breves al usuario.
     */
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    /**
     * Mensaje de confirmación para indicar que un municipio ha sido marcado como favorito (home).
     */
    val homeConfirmation by viewModel.homeConfirmationMessage.collectAsState()

    /**
     * Estado para controlar el host de Snackbar en Compose, responsable de mostrar los mensajes.
     */
    val snackbarHostState = remember { SnackbarHostState() }

    /**
     * Municipio actualmente seleccionado para mostrar su pronóstico horario detallado.
     * Es null cuando no hay ninguno seleccionado.
     */
    val selectedMunicipio = remember { mutableStateOf<SavedMunicipio?>(null) }

    /**
     * Municipio seleccionado para eliminar, que activa un diálogo de confirmación.
     */
    val municipioToDelete = remember { mutableStateOf<SavedMunicipio?>(null) }

    /**
     * Estado que almacena el texto introducido en el campo de búsqueda para añadir municipios.
     */
    var nameInput by remember { mutableStateOf(TextFieldValue()) }

    /**
     * Booleano que controla la visibilidad del campo de búsqueda y sugerencias.
     */
    var showSearch by remember { mutableStateOf(false) }

    /**
     * Texto localizable que muestra una instrucción o ayuda para añadir municipios.
     */
    val promptTextAddMuni = stringResource(R.string.usa_barra_anyadir_muni)

    /**
     * Mapa mutable que almacena qué tarjetas de municipio están expandidas (true) o contraídas (false).
     * La clave es el ID del municipio.
     */
    val expandedCards = remember { mutableStateMapOf<String, Boolean>() }

    /**
     * Hora actual en formato "HH", usada para filtrar el pronóstico horario correcto para mostrar.
     */
    val currentHour = LocalTime.now().hour.toString().padStart(2, '0')

    /**
     * Pronóstico horario completo para el municipio seleccionado, correspondiente a la hora actual.
     * Puede ser null si no hay datos disponibles.
     */
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
            Text(
                stringResource(R.string.munis_guardados),
                style = MaterialTheme.typography.titleLarge
            )
            Row {
                IconButton(onClick = { showSearch = !showSearch }) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.buscar))
                }
                IconButton(onClick = { viewModel.reloadFromFirestore() }) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = stringResource(R.string.recargar)
                    )
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

        // Show message when no municipios
        if (municipios.isEmpty()) {
            Text(
                text = stringResource(R.string.usa_barra_anyadir_muni),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp)
            )
        }

        LazyColumn {
            items(municipios) { municipio ->
                val snapshotState = snapshotStates[municipio.id]

                Column(modifier = Modifier.fillMaxWidth()) {
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
                                        val wasExpanded = expandedCards[municipio.id] ?: false
                                        expandedCards[municipio.id] = !wasExpanded
                                        if (!wasExpanded) viewModel.fetchDailyForecast(municipio.id)
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

                    if (expandedCards[municipio.id] == true) {
                        val forecast = dailyForecasts[municipio.id]
                        forecast?.let {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp)
                            ) {
                                items(it) { daily ->
                                    DailyForecastCard(forecast = daily)
                                }
                            }
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
                title = { Text("¿${stringResource(R.string.eliminar)} ${municipio.nombre}?") },
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
