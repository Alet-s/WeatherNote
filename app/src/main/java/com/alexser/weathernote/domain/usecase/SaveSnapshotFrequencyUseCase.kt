package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import javax.inject.Inject

/**
 * Caso de uso para guardar la frecuencia de snapshots para un municipio específico.
 *
 * @property repository Repositorio encargado de persistir la configuración de frecuencia.
 */
class SaveSnapshotFrequencyUseCase @Inject constructor(
    private val repository: SnapshotConfigRepository
) {

    /**
     * Guarda la frecuencia de snapshots indicada para el municipio con el ID proporcionado.
     *
     * @param municipioId ID del municipio para el que se guarda la frecuencia.
     * @param frequency Frecuencia de snapshots a guardar.
     */
    suspend operator fun invoke(municipioId: String, frequency: SnapshotFrequency) {
        repository.saveSnapshotFrequency(municipioId, frequency)
    }
}
