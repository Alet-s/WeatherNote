package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SnapshotConfigRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SnapshotConfigRepository {

    override suspend fun getSnapshotFrequency(municipioId: String): SnapshotFrequency {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        return try {
            val doc = firestore.collection("users")
                .document(userId)
                .collection("snapshot_configs")
                .document(municipioId)
                .get()
                .await()

            val freqName = doc.getString("frequency") ?: return SnapshotFrequency.MANUAL
            SnapshotFrequency.valueOf(freqName)
        } catch (e: Exception) {
            SnapshotFrequency.MANUAL
        }
    }

    override suspend fun saveSnapshotFrequency(municipioId: String, frequency: SnapshotFrequency) {
        val userId = auth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val data = mapOf("frequency" to frequency.name)

        firestore.collection("users")
            .document(userId)
            .collection("snapshot_configs")
            .document(municipioId)
            .set(data)
            .await()
    }
}
