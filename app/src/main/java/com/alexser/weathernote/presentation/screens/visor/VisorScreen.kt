package com.alexser.weathernote.presentation.screens.visor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.presentation.components.LoadingIndicator
import com.alexser.weathernote.presentation.components.MunicipioDropdown
import com.alexser.weathernote.presentation.components.MetricsCheckboxes
import com.alexser.weathernote.presentation.components.SnapshotChart
import com.alexser.weathernote.presentation.screens.snapshot.SnapshotMunicipioViewModel

@Composable
fun VisorScreen(
    savedMunicipios: List<SavedMunicipio>,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedMunicipio by remember { mutableStateOf<SavedMunicipio?>(null) }

    val selectedMetrics = remember {
        mutableStateMapOf(
            "Temperature" to true,
            "Humidity" to false,
            "Precipitation" to false,
            "Wind Speed" to false
        )
    }

    LaunchedEffect(selectedMunicipio) {
        selectedMunicipio?.let {
            viewModel.loadSnapshotData(it.id, it.nombre)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MunicipioDropdown(savedMunicipios, selectedMunicipio) {
            selectedMunicipio = it
        }

        MetricsCheckboxes(selectedMetrics)

        if (uiState.snapshots.isEmpty()) {
            LoadingIndicator()
        } else {
            SnapshotChart(uiState.snapshots, selectedMetrics)
        }
    }
}