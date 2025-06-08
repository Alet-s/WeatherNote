package com.alexser.weathernote.presentation.screens.signUp


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexser.weathernote.data.firebase.AuthDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel encargado de gestionar la lógica y el estado de la pantalla de registro de usuario.
 *
 * Maneja la entrada del usuario, la validación de datos y la interacción con el datasource de autenticación.
 *
 * @property authDataSource Fuente de datos para operaciones de autenticación.
 */
@HiltViewModel
class SignupViewModel @Inject constructor(
    private val authDataSource: AuthDataSource
) : ViewModel() {

    /** Estado mutable de la UI expuesto como StateFlow inmutable para observadores. */
    private val _uiState = MutableStateFlow(SignupUiState())
    val uiState: StateFlow<SignupUiState> = _uiState

    /**
     * Reinicia el estado de la UI al estado inicial limpio.
     */
    fun resetState() {
        _uiState.value = SignupUiState()
    }

    /**
     * Actualiza el email en el estado de la UI cuando el usuario lo modifica.
     *
     * @param email Nuevo valor de email ingresado.
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    /**
     * Actualiza la contraseña en el estado de la UI cuando el usuario la modifica.
     *
     * @param password Nuevo valor de la contraseña ingresada.
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    /**
     * Actualiza la confirmación de contraseña en el estado de la UI cuando el usuario la modifica.
     *
     * @param confirm Nuevo valor de la confirmación de contraseña ingresada.
     */
    fun onConfirmPasswordChange(confirm: String) {
        _uiState.update { it.copy(confirmPassword = confirm) }
    }

    /**
     * Maneja el evento de clic en el botón de registro.
     *
     * Valida que las contraseñas coincidan y realiza la llamada a la fuente de datos
     * para registrar al usuario. Actualiza el estado para reflejar carga y posibles errores.
     *
     * En caso de éxito, envía un email de verificación y ejecuta la función `onSuccess`.
     *
     * @param onSuccess Callback que se ejecuta si el registro es exitoso.
     */
    fun onSignupClick(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (state.password != state.confirmPassword) {
            _uiState.update { it.copy(error = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val result = authDataSource.register(
                email = state.email.trim(),
                password = state.password
            )

            if (result.isSuccess) {
                val user = result.getOrNull()
                user?.sendEmailVerification()

                _uiState.update {
                    it.copy(isLoading = false)
                }

                onSuccess() // Navigate to "verify-email" screen
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                    )
                }
            }
        }
    }
}
