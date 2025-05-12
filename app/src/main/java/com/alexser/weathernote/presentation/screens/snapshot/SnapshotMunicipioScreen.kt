package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.components.SnapshotFrequencySelector
import com.alexser.weathernote.presentation.components.SnapshotReportItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotMunicipioScreen(
    municipio: SavedMunicipio,
    navController: NavController,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(municipio.id) {
        viewModel.loadSnapshotData(municipio.id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(municipio.nombre) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Snapshot Frequency", style = MaterialTheme.typography.titleMedium)

            SnapshotFrequencySelector(
                selectedFrequency = uiState.selectedFrequency,
                onFrequencySelected = { viewModel.updateFrequency(it) }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = { viewModel.saveFrequency() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save Configuration")
                }

                Button(
                    onClick = { viewModel.generateSnapshotManually(municipio) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Generate Snapshot")
                }
            }

            Divider()

            Text("Past Snapshots", style = MaterialTheme.typography.titleMedium)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.snapshots) { snapshot ->
                    SnapshotReportItem(snapshot)
                }
            }
        }
    }
}
