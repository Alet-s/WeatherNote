package com.alexser.weathernote.utils

import android.content.Context
import android.util.Log
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import com.alexser.weathernote.domain.repository.SnapshotReportRepository

/**
 * Clase responsable de gestionar la limpieza (eliminación) de snapshots meteorológicos almacenados,
 * según las preferencias de retención configuradas para cada municipio o la configuración global.
 *
 * @property context Contexto de Android necesario para acceder a las preferencias.
 * @property repository Repositorio para acceder y manipular los snapshots almacenados.
 */
class SnapshotCleanupManager(
    private val context: Context,
    private val repository: SnapshotReportRepository
) {

    /**
     * Ejecuta la limpieza de snapshots si es necesario, eliminando los snapshots antiguos
     * que excedan el límite configurado para cada municipio.
     *
     * Para cada municipio, obtiene la configuración de retención específica (si existe),
     * o la configuración global. Si el número de snapshots supera el máximo permitido,
     * elimina los más antiguos, conservando solo los más recientes.
     *
     * La operación se realiza de forma segura, capturando excepciones y registrando logs
     * con detalles de la limpieza realizada.
     */
    suspend fun runCleanupIfNeeded() {
        try {
            val prefs = SnapshotPreferences(context)
            val allSnapshots = repository.getAllSnapshotsReports()

            val grouped = allSnapshots.groupBy { it.municipioId }

            var totalDeleted = 0

            for ((municipioId, snapshots) in grouped) {
                val option = prefs.getRetentionForMunicipio(municipioId)
                    ?: prefs.getSnapshotRetention()
                    ?: SnapshotRetentionOption.KEEP_ALL

                val max = option.maxSnapshots

                if (max != null && snapshots.size > max) {
                    val sorted = snapshots.sortedByDescending { it.timestamp }
                    val toDelete = sorted.drop(max)

                    if (toDelete.isNotEmpty()) {
                        repository.deleteBatchSnapshots(toDelete)
                        totalDeleted += toDelete.size
                        Log.d(
                            "SNAPSHOT_CLEANUP",
                            "🧹 $municipioId → Deleted ${toDelete.size} snapshots (kept $max)"
                        )
                    }
                } else {
                    Log.d("SNAPSHOT_CLEANUP", "✅ $municipioId → No cleanup needed")
                }
            }

            Log.d("SNAPSHOT_CLEANUP", "✨ Cleanup complete. Total deleted: $totalDeleted")

        } catch (e: Exception) {
            Log.e("SNAPSHOT_CLEANUP", "❌ Cleanup failed: ${e.message}")
        }
    }
}
