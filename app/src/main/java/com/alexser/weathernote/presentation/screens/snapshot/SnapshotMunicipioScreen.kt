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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.R
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.presentation.components.SnapshotRangeSelector
import com.alexser.weathernote.presentation.components.SnapshotReportItem
import com.alexser.weathernote.presentation.components.SnapshotDetailDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SnapshotMunicipioScreen(
    municipio: SavedMunicipio,
    navController: NavController,
    viewModel: SnapshotMunicipioViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val snapshotCount = uiState.snapshots.size
    var selectedRange by remember(snapshotCount) {
        mutableStateOf(0f..(snapshotCount - 1).coerceAtLeast(0).toFloat())
    }

    val snapshotsToShow = if (uiState.snapshots.isEmpty()) {
        emptyList()
    } else {
        val start = selectedRange.start.toInt().coerceAtLeast(0)
        val end = selectedRange.endInclusive.toInt().coerceAtMost(snapshotCount - 1)
        uiState.snapshots.slice(start..end)
    }

    val selectedIndexes = remember { mutableStateListOf<Int>() }
    val selectionMode = selectedIndexes.isNotEmpty()
    val selectedReportIds = snapshotsToShow
        .mapIndexedNotNull { index, snapshot ->
            if (selectedIndexes.contains(index)) snapshot.reportId else null
        }

    var showNoteDialog by remember { mutableStateOf(false) }
    var selectedSnapshotId by remember { mutableStateOf<String?>(null) }
    var noteText by remember { mutableStateOf("") }

    var showDetailDialog by remember { mutableStateOf(false) }
    var selectedSnapshotForDetail by remember { mutableStateOf<SnapshotReport?>(null) }

    val succesFileSaving = stringResource(R.string.archivo_guardado_exito)
    val cancelledMessage = stringResource(R.string.cancelado_usuario)
    val xErasedSnapshots = stringResource(R.string.snaps_borrados)
    val snapBorradoMessage = stringResource(R.string.snap_borrado)

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri ->
            if (uri != null) {
                viewModel.saveSnapshotJsonToUri(context, uri, selectedReportIds)
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(succesFileSaving)
                }
            } else {
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(cancelledMessage)
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
        selectedIndexes.clear()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.municipioName ?: municipio.nombre) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary),
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.volver))
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
                    contentDescription = stringResource(R.string.generar_snap),
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.snapshots),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 18.sp
                )
                if (snapshotsToShow.isNotEmpty()) {
                    TextButton(
                        onClick = {
                            selectedIndexes.clear()
                            selectedIndexes.addAll(snapshotsToShow.indices)
                        }
                    ) {
                        Text(stringResource(R.string.seleccionar_todos))
                    }
                }
            }

            if (selectionMode) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                val toDelete = snapshotsToShow.filterIndexed { idx, snapshot -> selectedIndexes.contains(idx) }
                                viewModel.deleteSnapshotsInBatch(toDelete)
                                selectedIndexes.clear()
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("${toDelete.size} $xErasedSnapshots")
                                }
                            },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = stringResource(R.string.borrar_seleccion),
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
                                painter = painterResource(R.drawable.wi_cloud_down),
                                contentDescription = stringResource(R.string.descargar_seleccion)
                            )
                        }

                        Text(
                            text = "${selectedReportIds.size} ${stringResource(R.string.seleccionados)}",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (uiState.snapshots.isNotEmpty()) {
                SnapshotRangeSelector(
                    totalCount = snapshotCount,
                    selectedRange = selectedRange,
                    onRangeChange = { selectedRange = it },
                    getDateLabel = { index ->
                        uiState.snapshots.getOrNull(index)?.timestamp?.substringBefore("T") ?: "-"
                    }
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 96.dp)
            ) {
                itemsIndexed(snapshotsToShow) { index, snapshot ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = {
                                    if (selectionMode) toggleSelection(index)
                                    else {
                                        selectedSnapshotForDetail = snapshot
                                        showDetailDialog = true
                                    }
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
                                    snackbarHostState.showSnackbar(snapBorradoMessage)
                                }
                            },
                            modifier = Modifier.weight(1f),
                            onNoteClick = {
                                selectedSnapshotId = snapshot.reportId
                                noteText = snapshot.userNote ?: ""
                                showNoteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    if (showNoteDialog && selectedSnapshotId != null) {
        AlertDialog(
            onDismissRequest = { showNoteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    selectedSnapshotId?.let { viewModel.updateNoteForSnapshot(it, noteText) }
                    showNoteDialog = false
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showNoteDialog = false }) { Text("Cancel") }
            },
            title = { Text("Add Note") },
            text = {
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Your note") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }

    if (showDetailDialog && selectedSnapshotForDetail != null) {
        SnapshotDetailDialog(
            snapshot = selectedSnapshotForDetail!!,
            onDismiss = { showDetailDialog = false }
        )
    }
}
