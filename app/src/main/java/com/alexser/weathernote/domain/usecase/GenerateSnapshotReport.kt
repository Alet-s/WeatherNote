package com.alexser.weathernote.domain.usecase

import com.alexser.weathernote.domain.model.SnapshotReport
import com.alexser.weathernote.domain.repository.SnapshotReportRepository
import javax.inject.Inject

/**
 * Use case para generar (guardar) un informe meteorológico (SnapshotReport).
 *
 * @property repository Repositorio que gestiona el almacenamiento de los informes meteorológicos.
 */
class GenerateSnapshotReport @Inject constructor(
    private val repository: SnapshotReportRepository
) {

    /**
     * Guarda el informe meteorológico proporcionado en el repositorio.
     *
     * @param snapshot El objeto [SnapshotReport] que se desea guardar.
     */
    suspend operator fun invoke(snapshot: SnapshotReport) {
        repository.saveSnapshotReport(snapshot)
    }
}
