package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.R
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
/**
 * Pantalla para configurar las opciones de retención de snapshots meteorológicos.
 *
 * Permite al usuario seleccionar cuánto tiempo desea conservar los snapshots,
 * eligiendo entre varias opciones predefinidas (por ejemplo, mantener los últimos 15, 31 días, etc.).
 *
 * También permite seleccionar para qué municipios aplicar dicha política de retención.
 * Incluye botones para guardar la configuración o forzar la limpieza de snapshots antiguos.
 *
 * @param navController Controlador de navegación para manejar la navegación entre pantallas.
 * @param viewModel ViewModel que gestiona el estado y lógica de la pantalla, con inyección Hilt por defecto.
 */
@Composable
fun SnapshotConfigScreen(
    navController: NavController,
    viewModel: SnapshotConfigScreenViewModel = hiltViewModel()
) {
    val selectedOption by viewModel.selectedOption.collectAsState()
    val municipios by viewModel.allMunicipios.collectAsState()
    val selectedMunicipioIds by viewModel.selectedMunicipioIds.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val messageSaved = stringResource(R.string.retencion_guardada)
    val messageCleanup = stringResource(R.string.limpieza_marcha)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Spacer(modifier = Modifier.padding(4.dp))
        Text(stringResource(R.string.cuantos_snaps))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(SnapshotRetentionOption.values()) { option ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    RadioButton(
                        selected = selectedOption == option,
                        onClick = { viewModel.onOptionSelected(option) }
                    )
                    Text(
                        text = when (option) {
                            SnapshotRetentionOption.KEEP_15 -> stringResource(R.string.mantener_15)
                            SnapshotRetentionOption.KEEP_31 -> stringResource(R.string.mantener_mes)
                            SnapshotRetentionOption.KEEP_62 -> stringResource(R.string.mantener_2_meses)
                            SnapshotRetentionOption.KEEP_93 -> stringResource(R.string.mantener_3_meses)
                            SnapshotRetentionOption.KEEP_186 -> stringResource(R.string.mantener_6_meses)
                            SnapshotRetentionOption.KEEP_365 -> stringResource(R.string.mantener_1_anyo)
                            SnapshotRetentionOption.KEEP_ALL -> stringResource(R.string.mantener_todo)
                        }
                    )
                }
            }
        }

        Text(stringResource(R.string.aplicar_opciones_a))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            municipios.forEach { municipio ->
                val selected = selectedMunicipioIds.contains(municipio.id)
                FilterChip(
                    selected = selected,
                    onClick = { viewModel.toggleMunicipio(municipio.id) },
                    label = { Text(municipio.nombre) }
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    viewModel.saveOptionToSelectedMunicipios()
                    scope.launch {
                        snackbarHostState.showSnackbar(messageSaved)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.guardar))
            }

            Button(
                onClick = {
                    viewModel.enforceCleanup()
                    scope.launch {
                        snackbarHostState.showSnackbar(messageCleanup)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(stringResource(R.string.limpiar_snaps_pasados))
            }
        }

        Spacer(modifier = Modifier.padding(3.dp))
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        SnackbarHost(hostState = snackbarHostState)
    }
}
