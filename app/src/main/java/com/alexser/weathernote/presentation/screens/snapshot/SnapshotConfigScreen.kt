package com.alexser.weathernote.presentation.screens.snapshot

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.alexser.weathernote.data.local.SnapshotPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SnapshotConfigScreen(
    navController: NavController
) {
    val context = LocalContext.current
    val prefs = remember { SnapshotPreferences(context) }
    val scope = rememberCoroutineScope()

    val currentSavedPath = prefs.getDownloadPath() ?: "Download"
    var inputPath by remember { mutableStateOf(TextFieldValue(currentSavedPath)) }

    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Configuración de Snapshots") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Ruta de descarga para los informes JSON:",
                style = MaterialTheme.typography.bodyLarge
            )

            OutlinedTextField(
                value = inputPath,
                onValueChange = { inputPath = it },
                label = { Text("Ruta (relativa a /storage/emulated/0/)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    prefs.setDownloadPath(inputPath.text)
                    scope.launch {
                        snackbarHostState.showSnackbar("Ruta guardada: ${inputPath.text}")
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Guardar")
            }
        }
    }
}
