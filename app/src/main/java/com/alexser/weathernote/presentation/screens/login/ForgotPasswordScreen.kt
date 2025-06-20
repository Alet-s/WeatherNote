package com.alexser.weathernote.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alexser.weathernote.R

/**
 * Pantalla de recuperación de contraseña donde el usuario puede introducir su correo
 * electrónico para recibir un enlace de restablecimiento.
 *
 * @param viewModel ViewModel asociado que gestiona el estado y lógica de la recuperación.
 * @param onBackToLogin Callback que se ejecuta cuando el usuario quiere volver a la pantalla de login.
 */
@Composable
fun ForgotPasswordScreen(
    viewModel: ForgotPasswordViewModel,
    onBackToLogin: () -> Unit
) {
    val email = viewModel.email
    val error = viewModel.error
    val success = viewModel.success
    val isLoading = viewModel.isLoading

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icono de la app
        Image(
            painter = painterResource(id = R.drawable.main_logo),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .height(260.dp)
                .padding(bottom = 32.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            stringResource(R.string.recuperar_pass),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChange,
            label = { Text(stringResource(R.string.correo_electronico)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::onSubmit,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.recuperar_correo))
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.vovler_ini_sesion),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { onBackToLogin() }
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(top = 24.dp))
        }

        error?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 16.dp))
        }

        if (success) {
            Text(
                text = stringResource(R.string.correo_envi_revisa),
                color = Color.Green,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}