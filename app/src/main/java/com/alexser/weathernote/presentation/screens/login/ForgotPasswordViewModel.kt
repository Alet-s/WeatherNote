package com.alexser.weathernote.presentation.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsable de gestionar la lógica y el estado de la pantalla de
 * recuperación de contraseña.
 *
 * Se encarga de manejar el correo electrónico introducido por el usuario,
 * procesar la solicitud de recuperación, y comunicar el estado (cargando, éxito, error)
 * a la interfaz de usuario.
 *
 * @property authDataSource Fuente de autenticación que proporciona la función de
 * recuperación de contraseña mediante correo electrónico.
 */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    /** Correo electrónico introducido por el usuario */
    var email by mutableStateOf("")

    /** Estado de carga mientras se procesa la solicitud */
    var isLoading by mutableStateOf(false)

    /** Mensaje de error en caso de fallo */
    var error by mutableStateOf<String?>(null)

    /** Indicador de éxito cuando el correo fue enviado correctamente */
    var success by mutableStateOf(false)

    /**
     * Actualiza el valor del correo electrónico conforme el usuario lo modifica.
     *
     * @param newEmail El nuevo valor del campo de correo electrónico.
     */
    fun onEmailChange(newEmail: String) {
        email = newEmail
    }

    /**
     * Envía una solicitud para restablecer la contraseña utilizando el correo
     * proporcionado. Actualiza el estado de carga, error y éxito según el resultado.
     */
    fun onSubmit() {
        viewModelScope.launch {
            isLoading = true
            error = null
            success = false

            val result = authDataSource.sendPasswordResetEmail(email.trim())
            isLoading = false

            result.fold(
                onSuccess = { success = true },
                onFailure = { error = it.localizedMessage ?: "Error" }
            )
        }
    }
}
