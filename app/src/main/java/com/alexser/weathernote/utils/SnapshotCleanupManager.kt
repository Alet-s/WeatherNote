package com.alexser.weathernote.utils

import android.content.Context
import android.util.Log
import com.alexser.weathernote.data.local.SnapshotPreferences
import com.alexser.weathernote.domain.model.SnapshotRetentionOption
import com.alexser.weathernote.domain.repository.SnapshotReportRepository

class SnapshotCleanupManager(
    private val context: Context,
    private val repository: SnapshotReportRepository
) {
    suspend fun runCleanupIfNeeded() {
        try {
            val prefs = SnapshotPreferences(context)
            val retention = prefs.getSnapshotRetention() ?: SnapshotRetentionOption.KEEP_ALL
            val max = retention.maxSnapshots

            if (max != null) {
                val all = repository.getAllSnapshotsReports()
                    .sortedByDescending { it.timestamp }

                val toDelete = all.drop(max)

                if (toDelete.isNotEmpty()) {
                    repository.deleteBatchSnapshots(toDelete)
                    Log.d("SNAPSHOT_CLEANUP", "🧹 Deleted ${toDelete.size} old snapshots")
                } else {
                    Log.d("SNAPSHOT_CLEANUP", "✅ No cleanup needed")
                }
            } else {
                Log.d("SNAPSHOT_CLEANUP", "🔒 KEEP_ALL policy selected")
            }
        } catch (e: Exception) {
            Log.e("SNAPSHOT_CLEANUP", "❌ Cleanup failed: ${e.message}")
        }
    }
}
