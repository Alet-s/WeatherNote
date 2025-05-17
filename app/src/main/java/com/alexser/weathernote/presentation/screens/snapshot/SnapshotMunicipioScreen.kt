package com.alexser.weathernote.presentation.screens.snapshot

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.presentation.components.SnapshotReportItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SnapshotMunicipioScreen(
    municipio: SavedMunicipio,
    navController: NavController,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedIndexes = remember { mutableStateListOf<Int>() }
    val selectionMode = selectedIndexes.isNotEmpty()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val selectedReportIds = uiState.snapshots
        .mapIndexedNotNull { index, snapshot ->
            if (selectedIndexes.contains(index)) snapshot.reportId else null
        }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.saveSnapshotJsonToUri(context, uri, selectedReportIds)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Archivo guardado con éxito.")
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Cancelado por el usuario.")
                }
            }
        }
    )

    fun toggleSelection(index: Int) {
        if (selectedIndexes.contains(index)) {
            selectedIndexes.remove(index)
        } else {
            selectedIndexes.add(index)
        }
    }

    LaunchedEffect(municipio.id) {
        viewModel.loadSnapshotData(municipio.id, municipio.nombre)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.municipioName ?: municipio.nombre) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.size(54.dp),
                onClick = { viewModel.generateSnapshotManually(municipio) }
            ) {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = "Generate Snapshot",
                    modifier = Modifier.size(26.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (selectionMode) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val toDelete = uiState.snapshots.filter { selectedReportIds.contains(it.reportId) }
                                viewModel.deleteSnapshotsInBatch(toDelete)
                                selectedIndexes.clear()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("${toDelete.size} snapshots deleted")
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete selected",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }

                        IconButton(
                            onClick = {
                                val suggestedName = "WeatherSnapshots_${System.currentTimeMillis()}.json"
                                createDocumentLauncher.launch(suggestedName)
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Download selected"
                            )
                        }

                        Text(
                            text = "${selectedReportIds.size} selected",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }


                }
            }

            Text("Past Snapshots", style = MaterialTheme.typography.titleMedium, fontSize = 18.sp)

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                itemsIndexed(uiState.snapshots) { index, snapshot ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    if (selectionMode) toggleSelection(index)
                                },
                                onLongClick = {
                                    toggleSelection(index)
                                }
                            )
                    ) {
                        if (selectionMode) {
                            Checkbox(
                                checked = selectedIndexes.contains(index),
                                onCheckedChange = { toggleSelection(index) }
                            )
                        }
                        SnapshotReportItem(
                            snapshot = snapshot,
                            onDelete = {
                                viewModel.deleteSnapshot(snapshot)
                                selectedIndexes.remove(index)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Snapshot deleted")
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
