package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Caso de uso para guardar un informe meteorológico (SnapshotReport).
 *
 * @property repository Repositorio encargado de la persistencia de SnapshotReports.
 */
class SaveSnapshotReportUseCase @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Guarda el informe meteorológico proporcionado.
     *
     * @param snapshot Informe meteorológico a guardar.
     */
    suspend operator fun invoke(snapshot: SnapshotReport) {
        repository.saveSnapshotReport(snapshot)
    }
}
