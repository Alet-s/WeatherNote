package com.alexser.weathernote.data.local

import android.content.Context

class SnapshotPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("snapshot_config", Context.MODE_PRIVATE)

    fun getDownloadPath(): String? = prefs.getString("download_path", null)

    fun setDownloadPath(path: String) {
        prefs.edit().putString("download_path", path).apply()
    }
}
