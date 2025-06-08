package com.alexser.weathernote.presentation.screens.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R

/**
 * Pantalla que informa al usuario que se ha enviado un correo electrónico de verificación.
 *
 * Muestra un mensaje instructivo y un botón para continuar una vez que el usuario
 * haya confirmado su correo.
 *
 * @param onContinue Lambda que se ejecuta cuando el usuario pulsa el botón "Continuar".
 */
@Composable
fun VerifyEmailScreen(
    onContinue: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.email_mandado))
        Spacer(modifier = Modifier.height(16.dp))
        Text(stringResource(R.string.verifique_continuar))
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onContinue) {
            Text(stringResource(R.string.continuar))
        }
    }
}
