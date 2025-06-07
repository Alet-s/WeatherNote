package com.alexser.weathernote.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Fuente de datos para operaciones relacionadas con Firestore.
 *
 * Esta clase permite acceder a documentos almacenados en la colección `municipios`,
 * y está diseñada para integrarse con la autenticación de Firebase.
 *
 * @property firestore Instancia de [FirebaseFirestore] inyectada mediante Hilt.
 * @property auth Instancia de [FirebaseAuth], útil si se desea hacer filtrado por usuario autenticado.
 */
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    /**
     * Obtiene el código del municipio a partir de su nombre.
     *
     * Realiza una consulta a la colección `municipios` buscando por el campo `"name"`.
     *
     * @param name Nombre del municipio que se desea buscar.
     * @return El código del municipio si se encuentra, o `null` si no hay coincidencias.
     */
    suspend fun getMunicipioCodeByName(name: String): String? {
        val snapshot = firestore.collection("municipios")
            .whereEqualTo("name", name)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.getString("code")
    }

}
