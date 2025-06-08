package com.alexser.weathernote.presentation.screens.visor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alexser.weathernote.R
import com.alexser.weathernote.presentation.screens.municipios.MunicipiosScreenViewModel

/**
 * Pantalla que muestra una lista de municipios guardados.
 * El usuario puede seleccionar un municipio para navegar a la pantalla
 * donde podrá visualizar gráficas basadas en los datos del municipio.
 *
 * @param navController Controlador de navegación para cambiar de pantalla.
 * @param viewModel ViewModel que provee los datos de municipios.
 */
@Composable
fun VisorMunicipioListScreen(
    navController: NavController,
    viewModel: MunicipiosScreenViewModel = hiltViewModel()
) {
    val municipios = viewModel.municipios.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(stringResource(R.string.selecciona_municipio), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(municipios) { municipio ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("visor/${municipio.id}/${municipio.nombre}")
                        },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(municipio.nombre, style = MaterialTheme.typography.titleMedium)
                        Text("ID: ${municipio.id}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
