package com.alexser.weathernote.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R

/**
 * Composable que muestra un indicador de carga centrado verticalmente con un texto descriptivo.
 *
 * Utiliza un [CircularProgressIndicator] acompañado de un texto que indica que los datos se están cargando.
 * Ideal para mostrar estados de espera o carga en la interfaz.
 */
@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(stringResource(R.string.cargando_datos), style = MaterialTheme.typography.bodyMedium)
    }
}
