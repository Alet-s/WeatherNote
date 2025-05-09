package com.alexser.weathernote.data.firebase

import android.util.Log
import com.alexser.weathernote.domain.model.SavedMunicipio
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MunicipioSyncService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    private fun userCollection() = auth.currentUser?.uid?.let { uid ->
        firestore.collection("users").document(uid).collection("saved_municipios")
    }

    suspend fun uploadMunicipios(localMunicipios: List<SavedMunicipio>) {
        val uid = auth.currentUser?.uid ?: return
        val collection = firestore.collection("users").document(uid).collection("saved_municipios")

        // Step 1: Get all remote municipios
        val remoteDocs = collection.get().await()
        val remoteIds = remoteDocs.documents.mapNotNull { it.id }.toSet()
        val localIds = localMunicipios.map { it.id }.toSet()

        // Step 2: Delete remote municipios not in local list
        val toDelete = remoteIds - localIds
        toDelete.forEach { id ->
            collection.document(id).delete().await()
        }

        // Step 3: Upload or overwrite current local municipios
        for (municipio in localMunicipios) {
            collection.document(municipio.id).set(municipio).await()
        }
    }

    suspend fun downloadRemoteMunicipios(): List<SavedMunicipio> {
        val uid = auth.currentUser?.uid ?: return emptyList()
        val collection = firestore.collection("users").document(uid).collection("saved_municipios")
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(SavedMunicipio::class.java) }
    }


    suspend fun clearRemoteMunicipios() {
        val collection = userCollection() ?: return
        val docs = collection.get().await()
        docs.documents.forEach { it.reference.delete().await() }
    }
}
