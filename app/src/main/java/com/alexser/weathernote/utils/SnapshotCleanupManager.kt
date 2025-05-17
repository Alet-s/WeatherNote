package com.alexser.weathernote.utils

import android.content.Context
import android.util.Log
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import com.alexser.weathernote.domain.repository.SnapshotReportRepository

class SnapshotCleanupManager(
    private val context: Context,
    private val repository: SnapshotReportRepository
) {
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
                        Log.d("SNAPSHOT_CLEANUP", "🧹 $municipioId → Deleted ${toDelete.size} snapshots (kept $max)")
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
