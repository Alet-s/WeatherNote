package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class SnapshotReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : SnapshotReportRepository {

    override suspend fun saveSnapshotReport(snapshot: SnapshotReport) {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val reportWithId = if (snapshot.reportId.isBlank()) {
            snapshot.copy(reportId = UUID.randomUUID().toString()) // ✅ generate unique ID
        } else {
            snapshot
        }

        println("🔥 Saving report with ID: ${reportWithId.reportId}")

        firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
            .document(reportWithId.reportId) // ✅ use reportId as document ID
            .set(reportWithId)
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

        return result.documents.mapNotNull { doc ->
            doc.toObject(SnapshotReport::class.java)
        }
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

    override suspend fun deleteSnapshot(snapshot: SnapshotReport) {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
            .document(snapshot.reportId)
            .delete()
            .await()
    }

    override suspend fun deleteBatchSnapshots(snapshots: List<SnapshotReport>) {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val collectionRef = firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")

        for (snapshot in snapshots) {
            collectionRef.document(snapshot.reportId).delete().await()
        }
    }
}
