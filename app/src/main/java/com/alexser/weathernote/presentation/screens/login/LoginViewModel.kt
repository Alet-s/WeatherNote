package com.alexser.weathernote.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de inicio de sesión.
 *
 * Se encarga de manejar el estado de la interfaz (email, contraseña, errores, etc.)
 * y de coordinar el proceso de autenticación mediante [AuthDataSource].
 *
 * @property authDataSource Fuente de datos de autenticación, encargada de la lógica con Firebase.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())

    /**
     * Estado observable de la pantalla de login.
     */
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el campo de correo electrónico en el estado de la UI.
     *
     * @param email Nuevo correo electrónico introducido por el usuario.
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    /**
     * Actualiza el campo de contraseña en el estado de la UI.
     *
     * @param password Nueva contraseña introducida por el usuario.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    /**
     * Restablece el estado del formulario a sus valores iniciales.
     */
    fun resetState() {
        _uiState.value = LoginUiState()
    }

    /**
     * Inicia el proceso de autenticación con los datos introducidos.
     *
     * Si el login es exitoso, se invoca [onSuccess]. En caso de error,
     * se actualiza el estado con un mensaje de error.
     *
     * @param onSuccess Acción a ejecutar tras un login exitoso.
     */
    fun onLoginClick(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authDataSource.signIn(
                _uiState.value.email.trim(),
                _uiState.value.password
            )

            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage ?: "Error desconocido"
                    )
                }
            }
        }
    }
}
