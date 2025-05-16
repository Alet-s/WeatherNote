package com.alexser.weathernote.presentation.screens.snapshot

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotMunicipioScreen(
    municipio: SavedMunicipio,
    navController: NavController,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedIndexes = remember { mutableStateListOf<Int>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (!granted) {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar("Permission denied. Cannot save files.")
                }
            }
        }
    )

    fun requestStoragePermissionIfNeeded() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    LaunchedEffect(municipio.id) {
        viewModel.loadSnapshotData(municipio.id)
    }

    fun toggleSelection(index: Int) {
        if (selectedIndexes.contains(index)) {
            selectedIndexes.remove(index)
        } else {
            selectedIndexes.add(index)
        }
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
            val selectedReportIds = uiState.snapshots
                .mapIndexedNotNull { index, snapshot ->
                    if (selectedIndexes.contains(index)) snapshot.reportId else null
                }

            if (selectedReportIds.isNotEmpty()) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val toDelete = uiState.snapshots.filter { selectedReportIds.contains(it.reportId) }
                                viewModel.deleteSnapshotsInBatch(toDelete)
                                selectedIndexes.clear()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("${toDelete.size} snapshots deleted")
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                        ) {
                            Text("Eliminar seleccionados", color = MaterialTheme.colorScheme.onError)
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                requestStoragePermissionIfNeeded()
                                viewModel.downloadSnapshotsAsJsonById(context, selectedReportIds)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("${selectedReportIds.size} saved individually")
                                }
                            }
                        ) {
                            Text("Descargar como varios JSON")
                        }

                        Button(
                            onClick = {
                                requestStoragePermissionIfNeeded()
                                viewModel.downloadSnapshotsAsJsonBatchFile(context, selectedReportIds)
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Archivo combinado creado")
                                }
                            }
                        ) {
                            Text("Descargar como único archivo")
                        }
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
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = selectedIndexes.contains(index),
                            onCheckedChange = { toggleSelection(index) }
                        )
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


