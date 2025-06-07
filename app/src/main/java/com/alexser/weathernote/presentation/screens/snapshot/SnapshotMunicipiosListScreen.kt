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
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotMunicipiosListScreen(
    navController: NavController,
    viewModel: MunicipiosScreenViewModel = hiltViewModel()
) {
    val municipios by viewModel.municipios.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarMessage by viewModel.snackbarMessage.collectAsState()

    // Listen and show snackbar when message arrives
    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearSnackbarMessage()
        }
    }

    val importSnapshotLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.importSnapshotFromUri(context, it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Snapshots") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary),
                actions = {
                    IconButton(onClick = { navController.navigate("snapshotConfig") }) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.configuracion))
                    }
                    IconButton(
                        onClick = {
                            importSnapshotLauncher.launch(arrayOf("application/json"))
                        },
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Icon(painterResource(R.drawable.wi_cloud_up), contentDescription = stringResource(R.string.exportar_snaps))
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
