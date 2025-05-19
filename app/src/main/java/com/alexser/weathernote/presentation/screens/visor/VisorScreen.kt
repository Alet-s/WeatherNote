package com.alexser.weathernote.presentation.screens.visor

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.components.LoadingIndicator
import com.alexser.weathernote.presentation.components.MetricsCheckboxes
import com.alexser.weathernote.presentation.components.SnapshotChart
import com.alexser.weathernote.presentation.components.SnapshotRangeSelector
import com.alexser.weathernote.presentation.screens.snapshot.SnapshotMunicipioViewModel

@Composable
fun VisorScreen(
    municipio: SavedMunicipio,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val selectedMetrics = remember {
        mutableStateMapOf(
            "Temperature" to true,
            "Humidity" to false,
            "Precipitation" to false,
            "Wind Speed" to false
        )
    }

    val snapshotCount = uiState.snapshots.size
    var selectedRange by remember(snapshotCount) {
        mutableStateOf(0f..(snapshotCount - 1).coerceAtLeast(0).toFloat())
    }

    LaunchedEffect(municipio.id) {
        viewModel.loadSnapshotData(municipio.id, municipio.nombre)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("${municipio.nombre} - Visualización de Datos", style = MaterialTheme.typography.titleLarge)

        MetricsCheckboxes(selectedMetrics)

        if (uiState.snapshots.isEmpty()) {
            LoadingIndicator()
        } else {
            Spacer(modifier = Modifier.height(24.dp))

            SnapshotRangeSelector(
                totalCount = snapshotCount,
                selectedRange = selectedRange,
                onRangeChange = { selectedRange = it },
                getDateLabel = { index ->
                    uiState.snapshots.getOrNull(index)?.timestamp?.substringBefore("T") ?: "-"
                }
            )

            SnapshotChart(
                snapshots = uiState.snapshots.slice(
                    selectedRange.start.toInt()..selectedRange.endInclusive.toInt()
                ),
                selected = selectedMetrics
            )
        }
    }
}
