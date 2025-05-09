package com.alexser.weathernote.presentation.screens.municipios

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.presentation.components.WeatherCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MunicipiosScreen(
    viewModel: MunicipiosScreenViewModel
) {
    val municipios by viewModel.municipios.collectAsState()
    val snapshots by viewModel.snapshots.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var nameInput = remember { androidx.compose.runtime.mutableStateOf(TextFieldValue()) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Saved Municipios") })
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    value = nameInput.value,
                    onValueChange = { nameInput.value = it },
                    label = { Text("Municipio name") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    if (nameInput.value.text.isNotBlank()) {
                        viewModel.addMunicipioByName(nameInput.value.text)
                        nameInput.value = TextFieldValue()
                    }
                }) {
                    Text("Add")
                }
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
                                WeatherCard(report = snapshot)
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
        }
    }
}
