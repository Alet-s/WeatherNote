package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotFrequency
import com.alexser.weathernote.domain.repository.SnapshotConfigRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener la frecuencia de generación de snapshots para un municipio específico.
 *
 * Consulta el repositorio de configuración para recuperar la frecuencia de snapshots configurada
 * para el municipio identificado por [municipioId].
 *
 * @property repository Repositorio que gestiona la configuración de snapshots.
 */
class GetSnapshotFrequencyUseCase @Inject constructor(
    private val repository: SnapshotConfigRepository
) {
    /**
     * Obtiene la frecuencia de snapshots para el municipio dado.
     *
     * @param municipioId Identificador único del municipio.
     * @return La frecuencia de snapshots configurada para dicho municipio.
     */
    suspend operator fun invoke(municipioId: String): SnapshotFrequency {
        return repository.getSnapshotFrequency(municipioId)
    }
}
