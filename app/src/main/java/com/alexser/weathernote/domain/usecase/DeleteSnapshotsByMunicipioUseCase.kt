package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Use case para eliminar todos los informes meteorológicos (snapshots) asociados a un municipio específico.
 *
 * @property repository Repositorio que gestiona las operaciones de SnapshotReport.
 */
class DeleteSnapshotsByMunicipioUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Ejecuta la eliminación de todos los snapshots asociados al municipio indicado por su ID.
     *
     * @param municipioId ID del municipio cuyos snapshots serán eliminados.
     */
    suspend operator fun invoke(municipioId: String) {
        repository.deleteSnapshotsByMunicipioId(municipioId)
    }
}
