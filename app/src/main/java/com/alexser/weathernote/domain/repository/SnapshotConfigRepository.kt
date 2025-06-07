package com.alexser.weathernote.domain.repository

import com.alexser.weathernote.domain.model.SnapshotFrequency

/**
 * Repositorio para la configuración de la frecuencia de generación
 * de snapshots meteorológicos por municipio.
 */
interface SnapshotConfigRepository {

    /**
     * Obtiene la frecuencia de snapshot configurada para un municipio dado.
     *
     * @param municipioId Identificador único del municipio.
     * @return [SnapshotFrequency] configurada para ese municipio.
     */
    suspend fun getSnapshotFrequency(municipioId: String): SnapshotFrequency

    /**
     * Guarda la frecuencia de snapshot para un municipio dado.
     *
     * @param municipioId Identificador único del municipio.
     * @param frequency Frecuencia de snapshots a guardar.
     */
    suspend fun saveSnapshotFrequency(municipioId: String, frequency: SnapshotFrequency)
}
