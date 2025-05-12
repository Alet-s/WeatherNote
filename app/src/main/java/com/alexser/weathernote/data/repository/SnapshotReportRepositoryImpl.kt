package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SnapshotReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth

) : SnapshotReportRepository {

    override suspend fun saveSnapshotReport(snapshot: SnapshotReport) {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")
        println("🔥 Firebase userId: ${firebaseAuth.currentUser?.uid}")
        firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
            .add(snapshot)
            .await()

    }

    override suspend fun getAllSnapshotsReports(): List<SnapshotReport> {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val result = firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
            .get()
            .await()

        return result.toObjects(SnapshotReport::class.java)
    }

    override suspend fun deleteSnapshotsByMunicipioId(municipioId: String) {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val snapshotsRef = firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")

        val toDelete = snapshotsRef
            .whereEqualTo("municipioId", municipioId)
            .get()
            .await()

        toDelete.documents.forEach { it.reference.delete().await() }
    }

}
