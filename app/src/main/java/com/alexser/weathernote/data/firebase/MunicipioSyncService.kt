package com.alexser.weathernote.data.firebase

import com.alexser.weathernote.domain.model.SavedMunicipio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Clase encargada de sincronizar los municipios guardados en la memoria local
 * con la base de datos Firestore del usuario autenticado actualmente.
 */
class MunicipioSyncService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    /**
     * Sube la lista de municipios locales a Firestore, sincronizando los datos:
     * 1. Borra en Firestore los municipios que ya no existan localmente.
     * 2. Crea o sobrescribe los municipios locales en Firestore.
     */
    suspend fun uploadMunicipios(localMunicipios: List<SavedMunicipio>) {
        val uid = auth.currentUser?.uid ?: return // Si no hay usuario autenticado, termina
        val collection = firestore.collection("users").document(uid).collection("saved_municipios")

        // 1: Obtenemos los ID de los municipios remotos actualmente en Firestore
        val remoteDocs = collection.get().await()
        val remoteIds = remoteDocs.documents.mapNotNull { it.id }.toSet()

        // 2: Obtenemos los ID de los municipios locales
        val localIds = localMunicipios.map { it.id }.toSet()

        // 3: Determinamos los municipios que deban eliminarse de Firestore
        val toDelete = remoteIds - localIds
        toDelete.forEach { id ->
            collection.document(id).delete().await()
        }

        // 4: Subimos o sobreescribimos cada municipio en memoria dispositivo en Firestore
        for (municipio in localMunicipios) {
            collection.document(municipio.id).set(municipio).await()
        }
    }

    /**
     * Descarga los municipios guardados en Firestore para el usuario actual
     * y los convierte a objetos "SavedMunicipio".
     */
    suspend fun downloadRemoteMunicipios(): List<SavedMunicipio> {
        val uid = auth.currentUser?.uid
            ?: return emptyList() // Si no hay usuario autenticado, devuelve lista vacía
        val collection = firestore.collection("users").document(uid).collection("saved_municipios")
        val snapshot = collection.get().await()

        // Convierte cada documento recuperado en un objeto SavedMunicipio
        return snapshot.documents.mapNotNull { it.toObject(SavedMunicipio::class.java) }
    }
}
