package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

/**
 * Implementación de [SnapshotReportRepository] que gestiona la persistencia
 * de informes meteorológicos (snapshots) en Firestore.
 *
 * Utiliza Firebase Authentication para identificar al usuario actual
 * y almacenar sus datos de forma separada.
 *
 * @property firestore Instancia de Firestore para acceso a la base de datos.
 * @property firebaseAuth Instancia de FirebaseAuth para gestión de usuario autenticado.
 */
class SnapshotReportRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : SnapshotReportRepository {

    /**
     * Guarda o actualiza un [SnapshotReport] en Firestore.
     *
     * Si el informe no tiene un ID, se genera uno único con UUID.
     *
     * @param snapshot Informe meteorológico a guardar.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Obtiene todos los informes meteorológicos del usuario autenticado.
     *
     * @return Lista con todos los [SnapshotReport] guardados.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Elimina todos los informes relacionados con un municipio específico.
     *
     * @param municipioId ID del municipio cuyos informes se eliminarán.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Elimina un informe meteorológico específico.
     *
     * @param snapshot Informe a eliminar.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Elimina en lote varios informes meteorológicos.
     *
     * @param snapshots Lista de informes a eliminar.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Obtiene un informe meteorológico por su ID.
     *
     * @param reportId ID del informe a buscar.
     * @return El [SnapshotReport] si existe, o null si no se encontró.
     * @throws IllegalStateException si no hay usuario autenticado.
     */
    override suspend fun getSnapshotByReportId(reportId: String): SnapshotReport? {
        val userId = firebaseAuth.currentUser?.uid
            ?: throw IllegalStateException("User not authenticated")

        val doc = firestore.collection("users")
            .document(userId)
            .collection("snapshot_reports")
            .document(reportId)
            .get()
            .await()

        return if (doc.exists()) {
            doc.toObject(SnapshotReport::class.java)?.copy(reportId = doc.id)
        } else null
    }

}
