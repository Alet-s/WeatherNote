package com.alexser.weathernote.data.repository

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/**
 * Implementación de [SnapshotConfigRepository] que gestiona la configuración de
 * frecuencia de generación de snapshots por municipio, almacenada en Firestore.
 *
 * Usa Firebase Authentication para obtener el usuario actual y así acceder
 * a sus datos en Firestore.
 *
 * @property firestore Instancia de Firestore para acceder a la base de datos.
 * @property auth Instancia de FirebaseAuth para gestionar la autenticación de usuario.
 */
class SnapshotConfigRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : SnapshotConfigRepository {

    /**
     * Obtiene la frecuencia configurada para la generación automática de snapshots de un municipio.
     *
     * Si no existe configuración, devuelve [SnapshotFrequency.MANUAL] por defecto.
     *
     * @param municipioId Identificador del municipio.
     * @return Frecuencia configurada de tipo [SnapshotFrequency].
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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

    /**
     * Guarda o actualiza la frecuencia de generación automática de snapshots para un municipio.
     *
     * @param municipioId Identificador del municipio.
     * @param frequency Frecuencia a guardar de tipo [SnapshotFrequency].
     * @throws IllegalStateException si no hay usuario autenticado.
     */
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
