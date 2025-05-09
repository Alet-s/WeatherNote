package com.alexser.weathernote.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getMunicipioCodeByName(name: String): String? {
        val snapshot = firestore.collection("municipios")
            .whereEqualTo("name", name)
            .get()
            .await()

        return snapshot.documents.firstOrNull()?.getString("code")
    }

    suspend fun suggestMunicipiosByPrefix(prefix: String): List<String> {
        if (prefix.length < 2) return emptyList() // avoid expensive queries on very short input

        val snapshot = firestore.collection("municipios")
            .orderBy("name")
            .startAt(prefix)
            .endAt(prefix + "\uf8ff")
            .limit(10)
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.getString("name") }
    }

}
