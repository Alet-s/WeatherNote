package com.alexser.weathernote.data.local

import android.content.Context
import com.alexser.weathernote.domain.model.SnapshotRetentionOption

/**
 * Se encarga de gestionar las preferencias relacionadas con la retención de Snapshots.
 * Permite guardar y leer las políticas de retención, tanto de forma global como por municipio.
 * Estos datos se guardan en las preferencias locales del dispositivo Android.
 */
class SnapshotPreferences(context: Context) {

    // Accedemos a SharedPreferences con el nombre "snapshot_config"
    private val prefs = context.getSharedPreferences("snapshot_config", Context.MODE_PRIVATE)

    companion object {
        // Key para la política de retención global (modo antiguo o valor por defecto)
        private const val GLOBAL_KEY = "retention_policy"

        // Prefijo para asociar políticas de retención a municipios concretos
        private const val MUNICIPIO_PREFIX = "municipio_retention_"
    }

    /**Recupera los datos de la política de retención global (modo antiguo, usado como compatibilidad)*/
    fun getSnapshotRetention(): SnapshotRetentionOption? {
        val name = prefs.getString(GLOBAL_KEY, null)
        return name?.let { SnapshotRetentionOption.valueOf(it) }
    }

    /**Recupera los datos de la política de retención específica para un municipio*/
    fun getRetentionForMunicipio(municipioId: String): SnapshotRetentionOption? {
        val name = prefs.getString("$MUNICIPIO_PREFIX$municipioId", null)
        return name?.let { SnapshotRetentionOption.valueOf(it) }
    }

    /**Guarda una política de retención específica para un municipio*/
    fun setRetentionForMunicipio(municipioId: String, option: SnapshotRetentionOption) {
        prefs.edit().putString("$MUNICIPIO_PREFIX$municipioId", option.name).apply()
    }

    /*Elimina la política de retención específica de un municipio (se volvería a usar la global)*/
    fun removeRetentionForMunicipio(municipioId: String) {
        prefs.edit().remove("$MUNICIPIO_PREFIX$municipioId").apply()
    }

}
