package com.alexser.weathernote.presentation.screens.login

/**
 * Representa el estado de la interfaz de usuario para la pantalla de inicio de sesión.
 *
 * Esta clase se utiliza para almacenar y gestionar los valores introducidos por el usuario,
 * así como los estados de carga y error que puedan producirse durante el proceso de autenticación.
 *
 * @property email Dirección de correo electrónico introducida por el usuario.
 * @property password Contraseña introducida por el usuario.
 * @property isLoading Indica si se está realizando el inicio de sesión (por ejemplo, mostrando un loader).
 * @property error Mensaje de error a mostrar en caso de fallo en el inicio de sesión, o null si no hay error.
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
