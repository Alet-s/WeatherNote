package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SnapshotReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : SnapshotReportRepository {
    override suspend fun saveSnapshotReport(snapshot: SnapshotReport) {
        firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
    }

    override suspend fun getAllSnapshotsReports(): List<SnapshotReport> {
        val result = firestore.collection("snapshots")
            .get()
            .await()
        return result.toObjects(SnapshotReport::class.java)
    }
}
