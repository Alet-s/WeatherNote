package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.R
import com.alexser.weathernote.presentation.components.MunicipioSnapshotListCard
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
            TopAppBar(
                title = { Text("Snapshots") },
                actions = {
                    IconButton(onClick = { navController.navigate("snapshotConfig") }) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.configuracion))
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(municipios) { municipio ->
                    MunicipioSnapshotListCard(municipio) {
                        navController.navigate(
                            "snapshotMunicipio/${municipio.id}/${municipio.nombre}"
                        )
                    }
                }
            }
        }
    }
}
