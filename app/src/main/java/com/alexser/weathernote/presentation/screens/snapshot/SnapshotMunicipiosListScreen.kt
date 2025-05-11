package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotMunicipiosListScreen(
    navController: NavController,
    viewModel: MunicipiosScreenViewModel = hiltViewModel()
) {
    val municipios by viewModel.municipios.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Snapshots") })
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(municipios) { municipio ->
                MunicipioCard(municipio) {
                    navController.navigate("snapshotMunicipio/${municipio.id}")
                }
            }
        }
    }
}


@Composable
fun MunicipioCard(
    municipio: SavedMunicipio,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = municipio.nombre, style = MaterialTheme.typography.titleMedium)
            Text(text = "Tap to manage snapshots", style = MaterialTheme.typography.bodyMedium)
        }
    }
}
