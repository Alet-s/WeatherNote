package com.alexser.weathernote.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Fuente de datos para operaciones de autenticación con Firebase.
 *
 * Esta clase proporciona funciones para iniciar sesión, registrar usuarios,
 * enviar correos de restablecimiento de contraseña, cerrar sesión y obtener
 * el usuario autenticado actualmente.
 *
 * @property auth Instancia de [FirebaseAuth] inyectada mediante Hilt.
 */
@Singleton
class AuthDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    /**
     * Inicia sesión con el correo y la contraseña proporcionados.
     *
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @return [Result] con el [FirebaseUser] autenticado si tiene éxito,
     * o un fallo con la excepción si ocurre un error.
     */
    suspend fun signIn(email: String, password: String): Result<FirebaseUser> = withContext(
        Dispatchers.IO
    ) {
        try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            Result.success(result.user!!)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registra un nuevo usuario con el correo y la contraseña proporcionados.
     *
     * @param email El correo electrónico del nuevo usuario.
     * @param password La contraseña del nuevo usuario.
     * @return [Result] con el [FirebaseUser] creado si tiene éxito,
     * o un fallo con la excepción si ocurre un error.
     */
    suspend fun register(email: String, password: String): Result<FirebaseUser> =
        withContext(Dispatchers.IO) {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                Result.success(result.user!!)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    /**
     * Envía un correo electrónico para restablecer la contraseña al usuario especificado.
     *
     * @param email El correo electrónico del usuario que desea restablecer su contraseña.
     * @return [Result] con [Unit] si la operación tiene éxito,
     * o un fallo con la excepción si ocurre un error.
     */
    suspend fun sendPasswordResetEmail(email: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cierra la sesión del usuario actualmente autenticado.
     */
    fun signOut() = auth.signOut()

    /**
     * TODO: para futuras implementaciones
     * Obtiene el usuario actualmente autenticado.
     *
     * @return El [FirebaseUser] actual si hay sesión iniciada, o `null` si no la hay.
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}
