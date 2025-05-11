package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.presentation.components.SnapshotFrequencySelector
import com.alexser.weathernote.presentation.components.SnapshotReportItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotMunicipioScreen(
    municipioId: String,
    navController: NavController,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Load data when entering screen
    LaunchedEffect(municipioId) {
        viewModel.loadSnapshotData(municipioId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.municipioName ?: "Municipio") },
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
