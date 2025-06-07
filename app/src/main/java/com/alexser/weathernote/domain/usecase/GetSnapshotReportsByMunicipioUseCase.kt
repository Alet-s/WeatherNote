package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener todos los informes (snapshots) de un municipio específico.
 *
 * Recupera todos los SnapshotReports del repositorio y filtra solo aquellos
 * que pertenecen al municipio indicado por [municipioId].
 *
 * @property repository Repositorio que gestiona los SnapshotReports.
 */
class GetSnapshotReportsByMunicipioUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Obtiene la lista de SnapshotReports correspondientes al municipio indicado.
     *
     * @param municipioId Identificador único del municipio.
     * @return Lista de SnapshotReports asociados al municipio.
     */
    suspend operator fun invoke(municipioId: String): List<SnapshotReport> {
        return repository.getAllSnapshotsReports()
            .filter { it.municipioId == municipioId }
    }
}
