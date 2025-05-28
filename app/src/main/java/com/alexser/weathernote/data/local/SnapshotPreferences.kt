package com.alexser.weathernote.data.local

import android.content.Context
import com.alexser.weathernote.domain.model.SnapshotRetentionOption

class SnapshotPreferences(context: Context) {
    private val prefs = context.getSharedPreferences("snapshot_config", Context.MODE_PRIVATE)

    companion object {
        private const val GLOBAL_KEY = "retention_policy"
        private const val MUNICIPIO_PREFIX = "municipio_retention_"
    }

    // 🔹 Global fallback (for backward compatibility)
    fun getSnapshotRetention(): SnapshotRetentionOption? {
        val name = prefs.getString(GLOBAL_KEY, null)
        return name?.let { SnapshotRetentionOption.valueOf(it) }
    }

    // 🔹 Per-municipio getter/setter
    fun getRetentionForMunicipio(municipioId: String): SnapshotRetentionOption? {
        val name = prefs.getString("$MUNICIPIO_PREFIX$municipioId", null)
        return name?.let { SnapshotRetentionOption.valueOf(it) }
    }

    fun setRetentionForMunicipio(municipioId: String, option: SnapshotRetentionOption) {
        prefs.edit().putString("$MUNICIPIO_PREFIX$municipioId", option.name).apply()
    }

    fun removeRetentionForMunicipio(municipioId: String) {
        prefs.edit().remove("$MUNICIPIO_PREFIX$municipioId").apply()
    }

}
