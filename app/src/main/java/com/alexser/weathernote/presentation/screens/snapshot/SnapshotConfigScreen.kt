package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Snapshot Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text("How many snapshots should be kept?")

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
                                SnapshotRetentionOption.KEEP_15 -> "Keep last 15"
                                SnapshotRetentionOption.KEEP_31 -> "Keep 1 month (31)"
                                SnapshotRetentionOption.KEEP_62 -> "Keep 2 months (62)"
                                SnapshotRetentionOption.KEEP_93 -> "Keep 3 months (93)"
                                SnapshotRetentionOption.KEEP_186 -> "Keep 6 months (186)"
                                SnapshotRetentionOption.KEEP_365 -> "Keep 1 year (365)"
                                SnapshotRetentionOption.KEEP_ALL -> "Keep everything"
                            }
                        )
                    }
                }
            }

            Text("Apply this setting to:")

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
                            snackbarHostState.showSnackbar("Retention saved for selected municipios.")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        viewModel.enforceCleanup()
                        scope.launch {
                            snackbarHostState.showSnackbar("Cleanup triggered.")
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Enforce now")
                }
            }
        }
    }
}
