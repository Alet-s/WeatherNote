package com.alexser.weathernote.data.local

import android.content.Context
import com.alexser.weathernote.domain.model.SnapshotRetentionOption

class SnapshotPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("snapshot_config", Context.MODE_PRIVATE)

    fun getSnapshotRetention(): SnapshotRetentionOption? {
        val name = prefs.getString("retention_policy", null)
        return name?.let { SnapshotRetentionOption.valueOf(it) }
    }

    fun setSnapshotRetention(option: SnapshotRetentionOption) {
        prefs.edit().putString("retention_policy", option.name).apply()
    }
}
