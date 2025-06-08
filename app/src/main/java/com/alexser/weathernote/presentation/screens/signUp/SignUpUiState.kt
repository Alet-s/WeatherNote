package com.alexser.weathernote.presentation.screens.signUp

/**
 * Representa el estado de la UI para la pantalla de registro de usuario.
 *
 * Contiene los datos que se muestran y modifican en el formulario, así como
 * el estado de carga y posibles errores.
 *
 * @property email Correo electrónico ingresado por el usuario.
 * @property password Contraseña ingresada por el usuario.
 * @property confirmPassword Confirmación de la contraseña ingresada.
 * @property isLoading Indica si se está procesando una acción (ej. registro).
 * @property error Mensaje de error que se muestra si ocurre algún problema, o null si no hay error.
 */
data class SignupUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
