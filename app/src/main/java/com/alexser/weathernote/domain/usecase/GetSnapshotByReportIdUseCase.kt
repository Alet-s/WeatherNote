package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Caso de uso para obtener un [SnapshotReport] a partir de su identificador único.
 *
 * Consulta el repositorio para recuperar el informe meteorológico almacenado con
 * el [reportId] proporcionado.
 *
 * @property repository Repositorio que gestiona los [SnapshotReport].
 */
class GetSnapshotByReportIdUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Obtiene un informe meteorológico (snapshot) por su ID.
     *
     * @param reportId Identificador único del informe.
     * @return El [SnapshotReport] correspondiente si existe, o null si no se encuentra.
     */
    suspend operator fun invoke(reportId: String): SnapshotReport? {
        return repository.getSnapshotByReportId(reportId)
    }
}
