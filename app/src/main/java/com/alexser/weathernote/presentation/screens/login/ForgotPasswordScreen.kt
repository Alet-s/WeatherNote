package com.alexser.weathernote.presentation.screens.login

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
import androidx.compose.ui.unit.dp

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
        Text("Recuperar contraseña", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = viewModel::onSubmit,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar correo de recuperación")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Volver al inicio de sesión",
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
                text = "Correo enviado. Revisa tu bandeja de entrada.",
                color = Color.Green,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
